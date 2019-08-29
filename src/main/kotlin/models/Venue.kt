package models

data class Venue(
  val address1: String?,
  val address2: String?,
  val address3: String?,
  val city: String,
  val country: String,
  val id: Long,
  val name: String,
  val lat: Double,
  val lon: Double,
  val repinned: Boolean,
  val localizedCountryName: String,
  val phone: String?,
  val state: String?,
  val zip: String?
)
