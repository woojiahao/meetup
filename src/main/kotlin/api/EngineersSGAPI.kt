package api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import khttp.get
import models.engineers_sg.Event
import utility.extensions.read

class EngineersSGAPI : IMeetupAPI {
  private val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
  private val endpoint = "https://engineers.sg/api/events"

  private fun queryEvents(): List<Event> {
    val response = get(endpoint)
    if (response.statusCode >= 400) return emptyList()
    return gson
        .read<JsonObject>(response.text)["events"]
        .asJsonArray
        .map { gson.read<Event>(it.asJsonObject.toString()) }
  }

  override fun getEvents(size: Int) {
    val engineersSGEvents = queryEvents()
  }
}