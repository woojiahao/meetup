package api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import khttp.get
import models.GeneralEvent
import models.engineers_sg.Event
import utility.date
import utility.extensions.read
import utility.extractTimezoneDate
import utility.time
import utility.toSingaporeDateTime

class EngineersSGAPI : IMeetupAPI {
  private val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
  private val endpoint = "https://engineers.sg/api/events"

  private fun queryEvents(): List<Event> {
    with(get(endpoint)) {
      return if (statusCode >= 400) emptyList()
      else gson.read<JsonObject>(text)["events"].asJsonArray.map { gson.read<Event>(it.asJsonObject.toString()) }
    }
  }

  /***
   * Creates a [GeneralEvent] from [Event]
   */
  private fun Event.generalise(): GeneralEvent {
    val startDateTime = extractTimezoneDate(startTime).toSingaporeDateTime()
    val endDateTime = extractTimezoneDate(endTime).toSingaporeDateTime()
    return GeneralEvent(
      name,
      location,
      url,
      startDateTime.date,
      endDateTime.date,
      startDateTime.time,
      endDateTime.time
    )
  }

  override fun getEvents(size: Int) = queryEvents().map { it.generalise() }.subList(0, size + 1)
}