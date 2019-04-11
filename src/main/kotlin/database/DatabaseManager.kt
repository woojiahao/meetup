package database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

fun setup(databaseUrl: String) {
  Database.connect(databaseUrl, "org.postgresql.Driver")

  transaction {
    SchemaUtils.create(RegisteredChannels)
  }
}

class RegisteredChannel(val channelId: String, val serverId: String, val isEnabled: Boolean)

object RegisteredChannels : Table() {
  val channelId = varchar("channel_id", 20).primaryKey()
  val serverId = varchar("server_id", 20)
  val isEnabled = bool("is_enabled").default(true)
}