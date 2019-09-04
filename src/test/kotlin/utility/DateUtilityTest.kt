package utility

import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals

class DateUtilityTest {
  @Test
  fun `Time strings produce time based on Singapore zone`() {
    val dateTime = extractTimezoneDate("2019-09-03T18:30:00+08:00")
    assertEquals(2019, dateTime.year)
    assertEquals(9, dateTime.monthValue)
    assertEquals(3, dateTime.dayOfMonth)
    assertEquals(18, dateTime.hour)
    assertEquals(30, dateTime.minute)
    assertEquals(0, dateTime.second)
    assertEquals(ZoneOffset.ofHours(8), dateTime.offset)
  }

  @Test
  fun `ZonedDateTime is converted to Singapore date time`() {
    val japanTime = extractTimezoneDate("2019-09-03T18:30:00+09:00")
    val singaporeTime = extractTimezoneDate("2019-09-03T17:30:00+08:00")
    assertEquals(singaporeTime, japanTime.toSingaporeDateTime())
  }
}