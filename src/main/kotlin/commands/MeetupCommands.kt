package commands

import api.EngineersSGAPI
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.IntegerArg
import models.GeneralEvent
import utility.date
import utility.singaporeDateTime
import utility.singaporeZone
import java.awt.Color
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

private val calendar = Calendar.getInstance(TimeZone.getTimeZone(singaporeZone)).apply {
  set(HOUR_OF_DAY, 10)
  set(MINUTE, 0)
  set(SECOND, 0)
  set(AM_PM, AM)
}

private fun generateTimer(events: List<GeneralEvent>, commandEvent: CommandEvent): ScheduledExecutorService {
  val currentTime = Date().time

  calendar.takeIf { it.time.time < currentTime }?.add(DATE, 1)

  val delay = calendar.time.time - currentTime

  return Executors.newScheduledThreadPool(1).apply {
    scheduleAtFixedRate({
      commandEvent.respond(
        eventsEmbed(
          "Events happening today",
          "Auto-generated list of events happening today",
          events
        )
      )
    }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS)
  }
}

@CommandSet("meetup")
fun meetupCommands() = commands {
  val engineersSGAPI = EngineersSGAPI()
  var dailyPostService: ScheduledExecutorService? = null

  command("upcoming") {
    description = "Displays the upcoming events in Singapore"
    expect(arg(IntegerArg, true, 10))
    execute {
      val eventsCount = it.args[0] as Int
      val events = engineersSGAPI.getEvents(eventsCount)
      it.respond(eventsEmbed("Upcoming Events", "Upcoming events happening in Singapore.", events))
    }
  }

  command("today") {
    description = "Displays the events happening today in Singapore"
    execute {
      val events = engineersSGAPI.getEvents(null)
      val todayEvents = events.filter { event -> event.startDate == singaporeDateTime.date }
      it.respond(eventsEmbed("Events happening today", "Events happening in Singapore today.", todayEvents))
    }
  }

  command("daily") {
    description = "Enables the daily task timer to send updates at 10am"
    execute {
      it.respond("Daily posts scheduled")
      dailyPostService?.shutdown()
      dailyPostService = generateTimer(engineersSGAPI.getEvents(null), it)
    }
  }
}

private fun eventsEmbed(title: String, description: String, events: List<GeneralEvent>) =
    embed {
      title(title)
      description(description)
      setFooter(
        "Events list generated at ${singaporeDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))}",
        "https://pbs.twimg.com/profile_images/939337823785594880/YZBJObdX_400x400.jpg"
      )
      color(Color.decode("#03fcbe"))

      if (events.isEmpty()) {
        field {
          name = "No events"
          value = "There are no events currently."
        }
      } else {
        events.forEach {
          field {
            name = it.name
            value = "on __${it.startDate}__ at __${it.startTime}__ by ${it.organiser}\n[Learn more](${it.url})"
          }
        }
      }
    }
