package utility

import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

val singaporeZone = ZoneId.of("Asia/Singapore") ?: throw IllegalArgumentException("Zone ID for Singapore invalid")

val singaporeDateTime: ZonedDateTime
  get() = ZonedDateTime.now(singaporeZone)

fun extractTimezoneDate(timezoneString: String): Date =
  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
    .apply { timeZone = TimeZone.getTimeZone("GMT") }
    .parse(timezoneString)
