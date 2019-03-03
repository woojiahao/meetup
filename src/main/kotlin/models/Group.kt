package models

data class Group(
  val country: String,
  val id: Long,
  val joinMode: JoinMode,
  val lat: Double,
  val localizedLocation: String,
  val lon: Double,
  val name: String,
  val region: String,
  val state: String,
  val timezone: String,
  val urlName: String,
  val who: String
) {
  enum class JoinMode {
    OPEN, APPROVAL, CLOSED
  }
}