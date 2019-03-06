package commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.arg
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.command.arguments.IntegerArg
import meetup.Meetup
import models.Event
import utility.singaporeDateTime
import java.awt.Color
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

@CommandSet("meetup")
fun meetupCommands() = commands {
  val categories = listOf(292)
  val meetup = Meetup()

  command("upcoming") {
    description = "Get upcoming tech events from Meetup.com"
    expect(arg(IntegerArg("Page number"), true, 20))
    execute {
      with(it) {
        val pageCount = args.component1() as Int

        if (pageCount !in 0..40) {
          respond("$pageCount is invalid, try a number within the range of 0 to 40")
          return@execute
        }

        val upcomingEvents = meetup.findUpcomingEvents(pageCount, categories)
        respond(eventsEmbed("Upcoming events", upcomingEvents))
      }
    }
  }

  command("today") {
    description = "Get tech events happening today from Meetup.com"
    execute {
      val upcomingEvents = meetup.findUpcomingEvents(20, categories, singaporeDateTime)
      it.respond(eventsEmbed("Events happening today", upcomingEvents))
    }
  }
}

private fun eventsEmbed(title: String, events: List<Event>) =
  embed {
    val meetupLogoUrl = "https://secure.meetupstatic.com/s/img/786824251364989575000/logo/swarm/m_swarm_630x630.png"

    title(title)
    color(Color.decode("#70ffb9"))

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mma")
    val footer = "Created on: ${singaporeDateTime.format(dateFormatter)}"
    setFooter(footer, meetupLogoUrl)

    if (events.isEmpty()) description("No events available \uD83D\uDE22")

    events
      .sortedBy { it.localDate }
      .forEach { event ->
        field {
          val date = SimpleDateFormat("dd MMMM yyyy").format(event.localDate)

          name = event.name
          value = "on __${date}__ at __${event.localTime}__ by ${event.group?.name}\n[Learn more](${event.link})"
        }
      }
  }