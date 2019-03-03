package models

import com.google.gson.annotations.SerializedName

data class FeaturedPhoto(
  val baseUrl: String,
  val id: Long,
  val photoLink: String?,
  val thumbLink: String?,
  @SerializedName("highres_link") val highResLink: String?,
  val type: Type
) {
  enum class Type {
    EVENT, MEMBER
  }
}