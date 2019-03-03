package models

data class Fee(
  val accepts: Accept,
  val amount: Double,
  val currency: String,
  val description: String,
  val label: String,
  val required: Boolean
) {
  enum class Accept {
    PAYPAL, WEPAY, CASH
  }
}