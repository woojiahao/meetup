package meetup

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import configuration.meetupApiToken
import khttp.get
import com.google.gson.JsonObject
import models.Event
import utility.read

class Meetup {
  private val meetupApiBaseUrl = "http://api.meetup.com/"
  private val gson = GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create()

  fun findUpcomingEvents(pageCount: Int = 20): List<Event> {
    val parameters = mapOf(
      "topic_category" to "Tech",
      "page" to pageCount.toString(),
      "fields" to "featured_photo",
      "key" to meetupApiToken
    )

    val endpoint = constructEndpoint("find", "upcoming_events")
    val r = get(endpoint, params = parameters)

    val result = gson.read<JsonObject>(r.text)

    return result["events"].asJsonArray.map { gson.read<Event>(it.asJsonObject.toString()) }
  }

  private fun constructEndpoint(vararg path: String) = "$meetupApiBaseUrl${path.joinToString("/")}"
}