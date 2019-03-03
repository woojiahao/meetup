package commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.arg
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.command.arguments.IntegerArg
import meetup.Meetup
import models.Event
import java.awt.Color
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@CommandSet("meetup")
fun meetupCommands() = commands {
  val meetup = Meetup()

  command("upcoming") {
    description = "Get upcoming events from Meetup.com"
    expect(arg(IntegerArg("Page number"), true, 5))
    execute {
      with(it) {
        val pageCount = args.component1() as Int

        if (pageCount < 0 || pageCount > 40) {
          it.respond("$pageCount is invalid, try a number within the range of 0 to 40")
          return@execute
        }

        val upcomingEvents = meetup.findUpcomingEvents(pageCount)
        it.respond(upcomingEventsEmbed(jda.selfUser.avatarUrl, upcomingEvents))
      }
    }
  }

  command("today") {
    description = "Retrieve all the events that's happening today from Meetup.com"
    execute {

    }
  }
}

private fun upcomingEventsEmbed(avatarUrl: String?, events: List<Event>) =
  embed {
    title("Upcoming Events")
    color(Color.decode("#70ffb9"))

    val currentDateSingapore = ZonedDateTime.now(ZoneId.of("Asia/Singapore"))
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")
    val footer = "Created on: ${currentDateSingapore.format(dateTimeFormatter)}"
    setFooter(footer, avatarUrl)

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