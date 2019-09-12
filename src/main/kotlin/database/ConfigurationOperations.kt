package database

import database.ConfigurationName.DAILY_POST_TIMING_HOUR
import database.ConfigurationName.DAILY_POST_TIMING_MINUTE
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

data class DailyPostTiming(val hour: Int, val minute: Int)

private fun dailyPostTimingCheck(dailyPostTiming: DailyPostTiming) {
  require(dailyPostTiming.hour in 0..23) { "Daily post timing hour must be between 0 and 23" }
  require(dailyPostTiming.minute in 0..59) { "Daily post timing minute must be between 0 and 59" }
}

fun getDailyPostTiming() =
  transaction {
    val dailyPostTimingHour = Configurations.select { Configurations.configurationName eq DAILY_POST_TIMING_HOUR.name }
      .first()[Configurations.configurationValue]
      .toIntOrNull() ?: throw IllegalArgumentException("Hour must be an integer")

    val dailyPostTimingMinute = Configurations.select { Configurations.configurationName eq DAILY_POST_TIMING_MINUTE.name }
      .first()[Configurations.configurationValue]
      .toIntOrNull() ?: throw IllegalArgumentException("Minute must be an integer")

    val dailyPostTiming = DailyPostTiming(dailyPostTimingHour, dailyPostTimingMinute)
    dailyPostTimingCheck(dailyPostTiming)
    dailyPostTiming
  }

fun setDailyPostTiming(dailyPostTiming: DailyPostTiming): Unit =
  transaction {
    val hasDailyPostTimingSet = Configurations
      .select {
        (Configurations.configurationName eq DAILY_POST_TIMING_HOUR.name) and
          (Configurations.configurationName eq DAILY_POST_TIMING_MINUTE.name)
      }
      .fetchSize == 2

    dailyPostTimingCheck(dailyPostTiming)

    val hour = dailyPostTiming.hour.toString()
    val minute = dailyPostTiming.minute.toString()

    if (hasDailyPostTimingSet) {
      Configurations.update({ Configurations.configurationName eq DAILY_POST_TIMING_HOUR.name }) {
        it[Configurations.configurationValue] = hour
      }

      Configurations.update({ Configurations.configurationName eq DAILY_POST_TIMING_MINUTE.name }) {
        it[Configurations.configurationValue] = minute
      }
    } else {
      Configurations.insert {
        it[Configurations.configurationName] = DAILY_POST_TIMING_HOUR.name
        it[Configurations.configurationValue] = hour
      }

      Configurations.insert {
        it[Configurations.configurationName] = DAILY_POST_TIMING_MINUTE.name
        it[Configurations.configurationValue] = minute
      }
    }
  }
