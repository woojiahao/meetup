package meetup

import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import configuration.meetupApiToken
import khttp.get
import models.Event
import utility.extensions.read
import utility.singaporeDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Meetup {
  private val meetupApiBaseUrl = "http://api.meetup.com/"
  private val gson = GsonBuilder().setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create()

  private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

  fun findUpcomingEvents(
    pageCount: Int = 20,
    categories: List<Int> = emptyList(),
    on: ZonedDateTime? = null
  ): List<Event> {
    val fields = listOf("featured_photo", "group_topics")

    val parameters = mapOf(
      "topic_category" to categories.joinToString(","),
      "page" to pageCount,
      "fields" to fields.joinToString(","),
      "start_date_range" to on?.minusDays(1)?.format(dateFormat),
      "end_date_range" to on?.plusDays(1)?.format(dateFormat),
      "key" to meetupApiToken
    ).mapNotNull { item -> item.value?.let { Pair(item.key, item.value.toString()) } }.toMap()

    val endpoint = constructEndpoint("find", "upcoming_events")
    val r = get(endpoint, params = parameters)

    if (r.statusCode >= 400) return emptyList()

    return gson
      .read<JsonObject>(r.text)["events"]
      .asJsonArray
      .map { gson.read<Event>(it.asJsonObject.toString()) }
  }

  private fun constructEndpoint(vararg path: String) = "$meetupApiBaseUrl${path.joinToString("/")}"
}