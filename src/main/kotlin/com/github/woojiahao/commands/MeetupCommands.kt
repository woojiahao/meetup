package com.github.woojiahao.commands

import api.EngineersSGAPI
import database.getRegisteredChannels
import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.arg
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.arguments.IntegerArg
import models.GeneralEvent
import utility.date
import utility.singaporeDateTime
import utility.singaporeZone
import utility.toEpochMilli
import java.awt.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

private fun scheduleDailyTask(timeToTrigger: LocalDateTime, action: () -> Unit): ScheduledExecutorService {
  val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mma")

  println("Generating timer event for daily posts")
  println("Current calendar is posting on ${timeToTrigger.format(formatter)}")

  val currentTime = singaporeDateTime.toInstant().toEpochMilli()

  val timeDifference = timeToTrigger.toEpochMilli(singaporeZone) - currentTime
  println("Time difference is $timeDifference")

  val postingTime = if (timeDifference < 0) timeToTrigger.plusDays(1) else timeToTrigger
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

      val timeToPost = LocalDate.now(singaporeZone).atTime(hour, minute)

      dailyPostService?.shutdown()

      dailyPostService = scheduleDailyTask(timeToPost) {
        val events = engineersSGAPI.getEvents(null).filter { event -> event.startDate == singaporeDateTime.date }

        println("Scheduling timer task to send updates")

        val registeredChannels = getRegisteredChannels().map { registeredChannel -> registeredChannel.channelId }
        println("Registered channels: ${registeredChannels.joinToString(",")}")
        val textChannels = registeredChannels.map { id -> it.discord.jda.getTextChannelById(id) }

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
