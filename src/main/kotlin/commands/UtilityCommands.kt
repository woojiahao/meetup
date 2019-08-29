package commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import java.awt.Color

@CommandSet("utility")
fun utilityCommands() = commands {
  command("ping") {
    description = "Displays bot ping"
    execute { it.respond("Pong! - ${it.jda.ping}ms") }
  }

  command("source") {
    description = "Source for meetup"
    execute { it.respond("https://github.com/woojiahao/meetup") }
  }

  command("about") {
    description = "Information for meetup"
    execute { it.respond(aboutEmbed()) }
  }
}

private fun aboutEmbed() =
  embed {
    title("About Meetup")
    color(Color.decode("#7E57C2"))

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