package com.github.woojiahao.commands

import dailyPostService
import database.DailyPostTiming
import database.getRegisteredChannels
import database.setDailyPostTiming
import engineersSGAPI
import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.arg
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.arguments.IntegerArg
import models.GeneralEvent
import net.dv8tion.jda.api.JDA
import utility.date
import utility.scheduleDailyTask
import utility.singaporeDateTime
import utility.singaporeZone
import java.awt.Color
import java.time.LocalDate

@CommandSet("meetup")
fun meetupCommands() = commands {
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
    description = "Enables the daily task timer to send updates at specified time"
    expect(IntegerArg("Hour"), IntegerArg("Minute"))
    execute {
      it.respond("Daily posts scheduled")

      val hour = it.args[0] as Int
      val minute = it.args[1] as Int

      if (hour !in 0..23) {
        it.respond("Hour must be between 0 and 23")
        return@execute
      }

      if (minute !in 0..59) {
        it.respond("Minute must be between 0 and 59")
        return@execute
      }

      val dailyPostTiming = DailyPostTiming(hour, minute)

      setDailyPostTiming(dailyPostTiming)

      scheduleDailyUpdate(dailyPostTiming, it.discord.jda)
    }
  }
}

fun scheduleDailyUpdate(dailyPostTiming: DailyPostTiming, jda: JDA) {
  val timeToPost = LocalDate.now(singaporeZone).atTime(dailyPostTiming.hour, dailyPostTiming.minute)

  dailyPostService?.shutdown()

  dailyPostService = scheduleDailyTask(timeToPost) {
    val events = engineersSGAPI.getEvents(null).filter { event -> event.startDate == singaporeDateTime.date }

    println("Scheduling timer task to send updates")

    val registeredChannels = getRegisteredChannels().map { registeredChannel -> registeredChannel.channelId }
    println("Registered channels: ${registeredChannels.joinToString(",")}")
    val textChannels = registeredChannels.map { id -> jda.getTextChannelById(id) }

    textChannels.forEach { textChannel ->
      println("Sending to ${textChannel?.name}")
      textChannel?.sendMessage(eventsEmbed(
        "Events happening today",
        "Events happening in Singapore today!",
        events
      ))?.queue()
    }
  }
}

private fun eventsEmbed(title: String, description: String, events: List<GeneralEvent>) =
  embed {
    this.title = title
    this.description = description
    this.color = Color.decode("#03fcbe")

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
