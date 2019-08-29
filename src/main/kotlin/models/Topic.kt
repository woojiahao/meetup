package models

import com.google.gson.annotations.SerializedName

data class Topic(
  val id: Long,
  val name: String,
  @SerializedName("urlkey") val urlKey: String,
  val lang: String
)
