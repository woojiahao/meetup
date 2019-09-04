package commands

import api.EngineersSGAPI
import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands

@CommandSet("meetup")
fun meetupCommands() = commands {
  command("upcoming") {
    execute {
      val api = EngineersSGAPI()
      val events = api.getEvents(10)
      val names = events.joinToString("\n") { event ->
        event.name
      }
      it.respond(names)
    }
  }
}
