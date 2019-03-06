package utility

import java.time.ZoneId
import java.time.ZonedDateTime

val singaporeZone = ZoneId.of("Asia/Singapore") ?: throw IllegalArgumentException("Zone ID for Singapore invalid")

val singaporeDateTime: ZonedDateTime
  get() = ZonedDateTime.now(singaporeZone)

