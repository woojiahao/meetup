package meetup

import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import configuration.meetupApiToken
import khttp.get
import models.Event
import utility.extensions.read
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Meetup {
  private val meetupApiBaseUrl = "http://api.meetup.com/"
  private val gson = GsonBuilder().setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create()

  fun findUpcomingEvents(
    pageCount: Int = 20,
    categories: List<String> = emptyList(),
    on: ZonedDateTime? = null
  ): List<Event> {
    val fields = listOf("featured_photo", "group_topics")

    val parameters = mapOf(
      "topic_category" to categories.joinToString(","),
      "page" to pageCount,
      "fields" to fields.joinToString(","),
      "key" to meetupApiToken
    ).mapNotNull { item -> item.value?.let { Pair(item.key, item.value.toString()) } }.toMap()

    val endpoint = constructEndpoint("find", "upcoming_events")
    val r = get(endpoint, params = parameters)

    if (r.statusCode >= 400) return emptyList()

    return gson
      .read<JsonObject>(r.text)["events"]
      .asJsonArray
      .map { gson.read<Event>(it.asJsonObject.toString()) }
      .filter { event ->
        on ?: return@filter true
        event.localDate ?: return@filter true
        on.toLocalDate() == event.localDate.toInstant().atZone(on.zone).toLocalDate()
      }
  }

  private fun constructEndpoint(vararg path: String) = "$meetupApiBaseUrl${path.joinToString("/")}"
}