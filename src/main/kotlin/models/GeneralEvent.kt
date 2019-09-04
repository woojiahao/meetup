package models

data class GeneralEvent(
  val name: String,
  val venue: String,
  val url: String,
  val startDate: String,
  val endDate: String,
  val startTime: String,
  val endTime: String
)