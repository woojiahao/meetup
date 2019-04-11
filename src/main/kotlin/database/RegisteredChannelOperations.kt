package database

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun registerChannel(channelId: String, serverId: String) =
  transaction {
    RegisteredChannels.insert {
      it[RegisteredChannels.channelId] = channelId
      it[RegisteredChannels.serverId] = serverId
    }
  }

fun unregisterChannel(channelId: String) =
  transaction {
    RegisteredChannels.deleteWhere { RegisteredChannels.channelId eq channelId }
  }

fun disableChannel(channelId: String) = toggleChannel(channelId, false)

fun enableChannel(channelId: String) = toggleChannel(channelId, true)

fun getRegisteredChannels() =
  transaction {
    RegisteredChannels
      .select { RegisteredChannels.isEnabled eq true }
      .map { it[RegisteredChannels.channelId] }
  }

private fun toggleChannel(channelId: String, state: Boolean) =
  transaction {
    RegisteredChannels.update({ RegisteredChannels.channelId eq channelId }) {
      it[RegisteredChannels.isEnabled] = state
    }
  }