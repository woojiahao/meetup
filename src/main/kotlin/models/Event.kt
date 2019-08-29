package models

import java.util.*

data class Event(
  val attendanceCount: Int?,
  val created: Long,
  val dateInSeriesPattern: Boolean,
  val description: String,
  val duration: Long,
  val featured: Boolean?,
  val featuredPhoto: FeaturedPhoto?,
  val fee: Fee?,
  val group: Group?,
  val id: String,
  val link: String,
  val localDate: Date?,
  val localTime: String?,
  val manualAttendanceCount: Int?,
  val name: String,
  val status: Status,
  val time: Long,
  val updated: Long?,
  val utcOffset: Long?,
  val venue: Venue?,
  val visibility: Visibility?
) {

  enum class Status {
    UPCOMING, CANCELLED, PAST, PROPOSED, SUGGESTED, DRAFT
  }

  enum class Visibility {
    PUBLIC, PUBLIC_LIMITED, MEMBERS
  }
}