package commands

import api.EngineersSGAPI
import database.getRegisteredChannels
import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.arg
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.command.arguments.IntegerArg
import models.GeneralEvent
import utility.date
import utility.singaporeDateTime
import utility.singaporeZone
import utility.toEpochMilli
import java.awt.Color
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

private val fixedTime = LocalDate.now(singaporeZone).atTime(10, 25)

private fun generateTimer(action: () -> Unit): ScheduledExecutorService {
  val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mma")

  println("Generating timer event for daily posts")
  println("Current calendar is posting on ${fixedTime.format(formatter)}")

  val currentTime = singaporeDateTime.toInstant().toEpochMilli()

  val timeDifference = fixedTime.toEpochMilli(singaporeZone) - currentTime
  println("Time difference is $timeDifference")

  val postingTime = if (timeDifference < 0) fixedTime.plusDays(1) else fixedTime
  println("Events to be posted on ${postingTime.format(formatter)}")

  val delay = postingTime.toEpochMilli(singaporeZone) - currentTime
  println("Delay will be $delay")

  return Executors.newScheduledThreadPool(1).apply {
    scheduleAtFixedRate({ action() }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS)
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
      dailyPostService = generateTimer() {
        val events = engineersSGAPI.getEvents(null)
        println("Scheduling timer task to send updates")
        val registeredChannels = getRegisteredChannels()
        println("Registered channels: ${registeredChannels.joinToString(",") { it.channelId }}")
        registeredChannels.forEach { channel ->
          println("Sending to $channel")
          it.jda.getTextChannelById(channel.channelId).sendMessage(
            eventsEmbed(
              "Events happening today",
              "Auto-generated list of events happening today",
              events
            )
          ).queue()
        }
      }
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
