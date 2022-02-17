package com.github.woojiahao.commands

import com.github.woojiahao.database.DailyPostTiming
import com.github.woojiahao.database.getRegisteredChannels
import com.github.woojiahao.database.setDailyPostTiming
import com.github.woojiahao.models.GeneralEvent
import com.github.woojiahao.utility.date
import com.github.woojiahao.utility.scheduleDailyTask
import com.github.woojiahao.utility.singaporeDateTime
import com.github.woojiahao.utility.singaporeZone
import com.github.woojiahao.dailyPostService
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.rest.builder.message.EmbedBuilder
import com.github.woojiahao.engineersSGAPI
import kotlinx.coroutines.runBlocking
import me.jakejmattson.discordkt.api.arguments.IntegerArg
import me.jakejmattson.discordkt.api.dsl.commands
import java.time.LocalDate
import dev.kord.common.Color as KordColor

fun meetup() = commands("Meetup") {
  command("upcoming") {
    description = "Displays the upcoming events in Singapore"
    execute(IntegerArg(name = "Events Count", description = "How many events to return")) {
      val eventsCount = args.first
      if (eventsCount > 10) {
        respond("Events count should be less than 10")
      } else {
        val events = engineersSGAPI.getEvents(eventsCount)
        respond {
          title = "Upcoming Events"
          description = "Upcoming events happening in Singapore."
          color = KordColor(3, 252, 190)
          fields = eventsToFields(events)
        }
      }
    }
  }

  command("today") {
    description = "Displays the events happening today in Singapore"
    execute {
      val events = engineersSGAPI.getEvents(null)
      val todayEvents = events.filter { event -> event.startDate == singaporeDateTime.date }
      respond {
        title = "Events happening today"
        description = "Events happening in Singapore today."
        color = KordColor(3, 252, 190)
        fields = eventsToFields(todayEvents)
      }
    }

    command("daily") {
      description = "Enables the daily task timer to send updates at specified time"
      execute(IntegerArg(name = "Hour"), IntegerArg(name = "Minute")) {
        respond("Daily posts scheduled")

        val hour = args.first
        val minute = args.second

        if (hour !in 0..23) {
          respond("Hour must be between 0 and 23")
          return@execute
        }

        if (minute !in 0..59) {
          respond("Minute must be between 0 and 59")
          return@execute
        }

        val dailyPostTiming = DailyPostTiming(hour, minute)

        setDailyPostTiming(dailyPostTiming)

        scheduleDailyUpdate(dailyPostTiming, discord.kord)
      }
    }
  }
}

fun scheduleDailyUpdate(dailyPostTiming: DailyPostTiming, kord: Kord) {
  val timeToPost = LocalDate.now(singaporeZone).atTime(dailyPostTiming.hour, dailyPostTiming.minute)

  dailyPostService?.shutdown()

  dailyPostService = scheduleDailyTask(timeToPost) {
    val events = engineersSGAPI.getEvents(null).filter { event -> event.startDate == singaporeDateTime.date }

    println("Scheduling timer task to send updates")

    val registeredChannels = getRegisteredChannels().map { registeredChannel -> registeredChannel.channelId }
    println("Registered channels: ${registeredChannels.joinToString(",")}")
    runBlocking {
      val textChannels = registeredChannels.mapNotNull {
        val mcb: MessageChannelBehavior? = kord.getChannelOf(id = Snowflake(it))
        return@mapNotNull mcb
      }

      textChannels.forEach { textChannel ->
        println("Sending to ${textChannel.mention}")
        textChannel.createEmbed {
          title = "Events happening today"
          description = "Events happening in Singapore today!"
          color = KordColor(3, 252, 190)
          fields = eventsToFields(events)
        }
      }
    }
  }
}

private fun eventsToFields(events: List<GeneralEvent>): MutableList<EmbedBuilder.Field> {
  return if (events.isEmpty()) {
    mutableListOf(EmbedBuilder.Field().apply {
      name = "No events"
      value = "There are no events currently."
    })
  } else {
    events.map {
      return@map EmbedBuilder.Field().apply {
        name = it.name
        value = "on __${it.startDate}__ at __${it.startTime}__ by ${it.organiser}\n[Learn more]($it.url)"
      }
    }.toMutableList()
  }
}

