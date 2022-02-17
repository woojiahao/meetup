package utility.extensions

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.Channel
import me.jakejmattson.discordkt.api.NoArgs
import me.jakejmattson.discordkt.api.dsl.CommandEvent

suspend fun CommandEvent<NoArgs>.getGuildById(id: String): Guild? = discord.kord.getGuild(Snowflake(id))
suspend fun CommandEvent<NoArgs>.getChannelById(id: String): Channel? = discord.kord.getChannel(Snowflake(id))
