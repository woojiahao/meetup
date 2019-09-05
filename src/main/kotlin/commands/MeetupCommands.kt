package commands

import api.EngineersSGAPI
import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.arg
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.command.arguments.IntegerArg
import models.GeneralEvent
import utility.date
import utility.singaporeDateTime
import java.awt.Color
import java.time.format.DateTimeFormatter

@CommandSet("meetup")
fun meetupCommands() = commands {
  val engineersSGAPI = EngineersSGAPI()

  command("upcoming") {
    expect(arg(IntegerArg, true, 10))
    execute {
      val eventsCount = it.args[0] as Int
      val events = engineersSGAPI.getEvents(eventsCount)
      it.respond(eventsEmbed("Upcoming Events", "Upcoming events happening in Singapore.", events))
    }
  }

  command("today") {
    execute {
      val events = engineersSGAPI.getEvents(null)
      val todayEvents = events.filter { event -> event.startDate == singaporeDateTime.date }
      it.respond(eventsEmbed("Events happening today", "Events happening in Singapore today", todayEvents))
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

      events.forEach {
        field {
          name = it.name
          value = "on __${it.startDate}__ at __${it.startTime}__ by ${it.organiser}\n[Learn more](${it.url})"
        }
      }
    }
