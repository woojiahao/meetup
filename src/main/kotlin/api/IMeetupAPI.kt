package api

import models.GeneralEvent

/***
 * Interface representing generic API calls for bot to meetup apis
 */
interface IMeetupAPI {
  fun getEvents(size: Int): List<GeneralEvent>
}