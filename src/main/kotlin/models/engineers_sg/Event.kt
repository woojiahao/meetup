package models.engineers_sg

data class Event(
  val id: String,
  val name: String,
  val description: String,
  val location: String,
  val url: String,
  val groupId: String,
  val groupName: String,
  val groupUrl: String,
  val formattedTime: String,
  val unixStartTime: Long,
  val startTime: String,
  val endTime: String,
  val platform: String,
  val rsvpCount: Int
)