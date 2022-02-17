package com.github.woojiahao.api

import com.github.kittinunf.fuel.httpGet
import com.github.woojiahao.models.GeneralEvent
import com.github.woojiahao.models.engineers_sg.Event
import com.github.woojiahao.utility.date
import com.github.woojiahao.utility.extensions.read
import com.github.woojiahao.utility.extractTimezoneDate
import com.github.woojiahao.utility.time
import com.github.woojiahao.utility.toSingaporeDateTime
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

class EngineersSGAPI : IMeetupAPI {
  private val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
  private val endpoint = "https://engineers.sg/api/events"

  private fun queryEvents(): List<Event> {
    val (_, response, _) = endpoint.httpGet().responseString()
    return if (response.statusCode >= 400) emptyList()
    else gson.read<JsonObject>(String(response.data))["events"].asJsonArray.map { gson.read<Event>(it.asJsonObject.toString()) }
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
        if (this.isEmpty()) emptyList<GeneralEvent>()
        else size?.let { subList(0, it + 1) } ?: this
      }
}