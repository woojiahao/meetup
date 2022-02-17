package commands

import database.getRegisteredChannels
import database.registerChannel
import database.unregisterChannel
import dev.kord.common.entity.Snowflake
import me.jakejmattson.discordkt.api.dsl.commands

fun configurations() = commands("Configurations") {
  command("register") {
    description = "Registers a channel to receive notifications"
    execute {
      val channelId = channel.id.asString
      val serverId = guild?.id?.asString ?: return@execute
      registerChannel(channelId, serverId)
      respond("Registered **${channel.data.name}** in **${guild?.name}** to receive notifications")
    }
  }

  command("unregister") {
    description = "Unregisters a channel from receiving notifications"
    execute {
      val channelId = channel.id.asString
      unregisterChannel(channelId)
      respond("Unregistered **${channel.data.name}** in **${guild?.name}** from receiving notifications")
    }
  }

  command("viewregistered") {
    description = "View all registered channels"
    execute {
      val registeredChannels = getRegisteredChannels()

      respond {
        title = "Registered Channels"
        description = "Channels registered will receive daily updates"
        registeredChannels.groupBy { it.serverId }.forEach { (serverId, channelIds) ->
          field {
            name = this@execute.discord.kord.getGuild(Snowflake(serverId))?.name ?: ""
            value = channelIds .map { it.channelId } .joinToString("\n")
          }
        }
      }
    }
  }
}