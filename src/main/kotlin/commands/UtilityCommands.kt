package commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands

@CommandSet("utility")
fun utilityCommands() = commands {
  command("ping") {
    description = "Displays bot ping"
    execute {
      it.respond("Pong! - ${it.jda.ping}ms")
    }
  }
}