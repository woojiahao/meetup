package com.github.woojiahao.api

import com.github.woojiahao.models.GeneralEvent

/***
 * Interface representing generic API calls for bot to meetup apis
 */
interface IMeetupAPI {
  fun getEvents(size: Int?): List<GeneralEvent>
}