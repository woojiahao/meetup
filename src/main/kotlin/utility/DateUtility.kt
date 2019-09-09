package utility

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val singaporeZone = ZoneId.of("Asia/Singapore") ?: throw IllegalArgumentException("Zone ID for Singapore invalid")

val singaporeDateTime: ZonedDateTime
  get() = ZonedDateTime.now(singaporeZone)

fun extractTimezoneDate(timezoneString: String): ZonedDateTime =
    ZonedDateTime.parse(timezoneString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXXXX"))

fun ZonedDateTime.toSingaporeDateTime(): ZonedDateTime =
    if (offset == ZoneOffset.ofHours(8)) this
    else withZoneSameInstant(ZoneId.ofOffset("", ZoneOffset.ofHours(8)))

fun LocalDateTime.toEpochMilli(zone: ZoneId) = atZone(zone).toInstant().toEpochMilli()

val ZonedDateTime.date
  get() = format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

val ZonedDateTime.time
  get() = format(DateTimeFormatter.ofPattern("hh.mma"))

