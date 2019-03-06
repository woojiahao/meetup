package utility

import java.time.ZoneId
import java.time.ZonedDateTime

val singaporeDateTime: ZonedDateTime
  get() = ZonedDateTime.now(ZoneId.of("Asia/Singapore"))

