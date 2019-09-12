package database

import database.Configuration.Name.DAILY_POST_TIMING_HOUR
import database.Configuration.Name.DAILY_POST_TIMING_MINUTE
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

data class DailyPostTiming(val hour: Int, val minute: Int)

private fun dailyPostTimingCheck(dailyPostTiming: DailyPostTiming) {
  require(dailyPostTiming.hour in 0..23) { "Daily post timing hour must be between 0 and 23" }
  require(dailyPostTiming.minute in 0..59) { "Daily post timing minute must be between 0 and 59" }
}

fun getConfiguration(configurationName: Configuration.Name): Configuration? =
  transaction {
    val matchingConfiguration = Configurations.select { Configurations.configurationName eq configurationName.name }

    if (matchingConfiguration.empty()) null
    else Configurations
      .select { Configurations.configurationName eq configurationName.name }
      .first()
      .let {
        Configuration(
          it[Configurations.index],
          it[Configurations.configurationName],
          it[Configurations.configurationValue]
        )
      }
  }

fun <T> updateConfiguration(configurationName: Configuration.Name, configurationValue: T) {
  transaction {
    Configurations.update({ Configurations.configurationName eq configurationName.name }) {
      it[Configurations.configurationValue] = configurationValue.toString()
    }
  }
}

fun <T> insertConfiguration(configurationName: Configuration.Name, configurationValue: T) {
  transaction {
    Configurations.insert {
      it[Configurations.configurationName] = configurationName.name
      it[Configurations.configurationValue] = configurationValue.toString()
    }
  }
}

fun <T> addConfiguration(configurationName: Configuration.Name, configurationValue: T) {
  val configuration = getConfiguration(configurationName)
  configuration
    ?.let { updateConfiguration(configurationName, configurationValue) }
    ?: insertConfiguration(configurationName, configurationValue)
}

fun getDailyPostTiming(): DailyPostTiming? =
  transaction {
    val dailyPostTimingHour = getConfiguration(DAILY_POST_TIMING_HOUR)
      ?.check("Hour must be an integer") { it.configurationValue.toIntOrNull() != null }
      ?.check("Hour must be between 0 and 23") { it.configurationValue.toInt() in 0..23 }
      ?.getValue { it.toInt() }

    val dailyPostTimingMinute = getConfiguration(DAILY_POST_TIMING_MINUTE)
      ?.check("Minute must be an integer") { it.configurationValue.toIntOrNull() != null }
      ?.check("Minute must be between 0 and 59") { it.configurationValue.toInt() in 0..59 }
      ?.getValue { it.toInt() }

    if (dailyPostTimingHour == null || dailyPostTimingMinute == null) null
    else DailyPostTiming(dailyPostTimingHour, dailyPostTimingMinute)
  }

fun setDailyPostTiming(dailyPostTiming: DailyPostTiming): Unit =
  transaction {
    dailyPostTimingCheck(dailyPostTiming)
    addConfiguration(DAILY_POST_TIMING_HOUR, dailyPostTiming.hour)
    addConfiguration(DAILY_POST_TIMING_MINUTE, dailyPostTiming.minute)
  }
