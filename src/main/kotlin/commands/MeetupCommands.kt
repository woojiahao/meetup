package commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands

@CommandSet("meetup")
fun meetupCommands() = commands {
  command("latest") {
    description = "Get the latest meetups from Meetup.com"
    execute {
      val meetupApiBaseUrl = "http://api.meetup.com"

      val meetupKey = System.getenv("MEETUP_API_KEY")

      val parameters = mapOf(
        "topic_category" to "Tech",
        "key" to meetupKey
      )

      val r = khttp.get("$meetupApiBaseUrl/find/upcoming_events", params = parameters)
      println(r.jsonObject)
    }
  }
}