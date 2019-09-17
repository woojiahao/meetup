package database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

fun setup(databaseUrl: String) {
  Database.connect(databaseUrl, "org.postgresql.Driver")

  transaction {
    SchemaUtils.create(RegisteredChannels, Configurations)
  }
}

class RegisteredChannel(val channelId: String, val serverId: String, val isEnabled: Boolean)

class Configuration(val index: Int, val name: String, val value: String) {
  enum class Name {
    DAILY_POST_TIMING_HOUR, DAILY_POST_TIMING_MINUTE
  }

  fun check(error: String, predicate: (Configuration) -> Boolean = { true }) =
    with(this) {
      require(predicate(this)) { error }
      this
    }

  fun <T> getValue(modification: (String) -> T) = modification(value)
}

object RegisteredChannels : Table() {
  val channelId = varchar("channel_id", 20).primaryKey()
  val serverId = varchar("server_id", 20)
  val isEnabled = bool("is_enabled").default(true)
}

object Configurations : Table() {
  val index = integer("index").uniqueIndex().primaryKey().autoIncrement()
  val configurationName = varchar("configuration_name", 256).primaryKey()
  val configurationValue = varchar("configuration_value", 256)
}