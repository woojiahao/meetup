package commands

import configuration.meetupApiToken
import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands

@CommandSet("meetup")
fun meetupCommands() = commands {
  command("latest") {
    description = "Get the latest meetups from Meetup.com"
    execute {
      val meetupApiBaseUrl = "http://api.meetup.com"

      val parameters = mapOf(
        "topic_category" to "Tech",
        "key" to meetupApiToken
      )

      val r = khttp.get("$meetupApiBaseUrl/find/upcoming_events", params = parameters)
      println(r.jsonObject)
    }
  }
}