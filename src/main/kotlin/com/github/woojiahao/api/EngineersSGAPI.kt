package com.github.woojiahao.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import khttp.get
import com.github.woojiahao.models.GeneralEvent
import com.github.woojiahao.models.engineers_sg.Event
import com.github.woojiahao.utility.date
import com.github.woojiahao.utility.extensions.read
import com.github.woojiahao.utility.extractTimezoneDate
import com.github.woojiahao.utility.time
import com.github.woojiahao.utility.toSingaporeDateTime

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
      groupName,
      url,
      startDateTime.date,
      endDateTime.date,
      startDateTime.time,
      endDateTime.time
    )
  }

  override fun getEvents(size: Int?): List<GeneralEvent> =
      with(queryEvents().map { it.generalise() }) {
        size?.let { subList(0, it + 1) } ?: this
      }
}