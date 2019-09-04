package commands

import database.RegisteredChannel
import database.getRegisteredChannels
import database.registerChannel
import database.unregisterChannel
import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import net.dv8tion.jda.core.JDA

@CommandSet("configuration")
fun configurationCommands() = commands {
  command("register") {
    description = "Registers a channel to receive notifications"
    execute {
      val channelId = it.channel.id
      val serverId = it.guild?.id ?: return@execute
      registerChannel(channelId, serverId)
      it.respond("Registered **${it.channel.name}** in **${it.guild?.name}** to receive notifications")
    }
  }

  command("unregister") {
    description = "Unregisters a channel from receiving notifications"
    execute {
      val channelId = it.channel.id
      unregisterChannel(channelId)
      it.respond("Unregistered **${it.channel.name}** in **${it.guild?.name}** from receiving notifications")
    }
  }

  command("viewregistered") {
    description = "View all registered channels"
    execute {
      val registeredChannels = getRegisteredChannels()
      it.respond(registeredChannelsEmbed(it.jda, registeredChannels))
    }
  }
}

private fun registeredChannelsEmbed(jda: JDA, registeredChannels: List<RegisteredChannel>) =
    embed {
      title("Registered Channels")
      description("Channels registered will receive the daily updates")
      registeredChannels.groupBy { it.serverId }.forEach { (serverId, channelIds) ->
        field {
          name = jda.getGuildById(serverId).name
          value = channelIds
              .map { it.channelId }
              .joinToString("\n") { "${jda.getTextChannelById(it).name} :: $it" }
        }
      }
    }