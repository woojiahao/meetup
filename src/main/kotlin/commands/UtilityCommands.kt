package commands

import me.jakejmattson.discordkt.api.dsl.commands
import dev.kord.common.Color as KordColor

fun utility() = commands("Utility") {
  command("ping") {
    description = "Displays bot ping"
    execute { respond("Pong! - ${discord.kord.gateway.averagePing}ms") }
  }

  command("source") {
    description = "Source for meetup"
    execute { respond("https://github.com/woojiahao/meetup") }
  }

  command("about") {
    description = "Information for meetup"
    execute {
      respond {
        title = "About Meetup"
        color = KordColor(126, 87, 194)

        field {
          name = "Description"
          value = listOf(
            "Meetup is a Discord bot for getting tech event information in Singapore!",
            "It serves the Singapore computer science discord, link [here](https://discord.gg/RRZeV5A)"
          ).joinToString("\n")
        }

        field {
          name = "APIs used"
          inline = true
          value = listOf(
            "[Meetup.com](https://www.meetup.com/meetup_api/)"
          ).joinToString("\n") { "- $it" }
        }

        field {
          name = "Creator"
          inline = true
          value = "Chill#4048"
        }

        field {
          name = "GitHub repository"
          inline = true
          value = "[Repository link](https://github.com/woojiahao/meetup)"
        }
      }
    }
  }
}
