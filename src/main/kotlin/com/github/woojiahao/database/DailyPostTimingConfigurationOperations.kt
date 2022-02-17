package com.github.woojiahao.database

import org.jetbrains.exposed.sql.transactions.transaction

data class DailyPostTiming(val hour: Int, val minute: Int)

private fun dailyPostTimingCheck(dailyPostTiming: DailyPostTiming) {
  require(dailyPostTiming.hour in 0..23) { "Daily post timing hour must be between 0 and 23" }
  require(dailyPostTiming.minute in 0..59) { "Daily post timing minute must be between 0 and 59" }
}

fun getDailyPostTiming(): DailyPostTiming? =
  transaction {
    val dailyPostTimingHour = getConfiguration(Configuration.Name.DAILY_POST_TIMING_HOUR)
      ?.check("Hour must be an integer") { it.value.toIntOrNull() != null }
      ?.check("Hour must be between 0 and 23") { it.value.toInt() in 0..23 }
      ?.getValue { it.toInt() }

    val dailyPostTimingMinute = getConfiguration(Configuration.Name.DAILY_POST_TIMING_MINUTE)
      ?.check("Minute must be an integer") { it.value.toIntOrNull() != null }
      ?.check("Minute must be between 0 and 59") { it.value.toInt() in 0..59 }
      ?.getValue { it.toInt() }

    if (dailyPostTimingHour == null || dailyPostTimingMinute == null) null
    else DailyPostTiming(dailyPostTimingHour, dailyPostTimingMinute)
  }

fun setDailyPostTiming(dailyPostTiming: DailyPostTiming): Unit =
  transaction {
    dailyPostTimingCheck(dailyPostTiming)
    addConfiguration(Configuration.Name.DAILY_POST_TIMING_HOUR, dailyPostTiming.hour)
    addConfiguration(Configuration.Name.DAILY_POST_TIMING_MINUTE, dailyPostTiming.minute)
  }
