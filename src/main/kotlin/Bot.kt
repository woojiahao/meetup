import commands.eventsEmbed
import configuration.botToken
import configuration.channels
import me.aberrantfox.kjdautils.api.startBot
import meetup.Meetup
import net.dv8tion.jda.core.JDA
import utility.singaporeDateTime
import utility.singaporeZone
import java.awt.Color
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

private val meetup = Meetup()

fun main() {
  val jda = startBot(botToken) { configure { prefix = "!" } }.jda
  configureDailyPost(jda)
}

private fun configureDailyPost(jda: JDA) {
  with(Calendar.getInstance(TimeZone.getTimeZone(singaporeZone))) {
    set(Calendar.HOUR_OF_DAY, 10)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)

    val postTask = timerTask {
      val todayEvents = meetup.findUpcomingEvents(20, listOf(292), singaporeDateTime)
      channels.forEach { channel ->
        jda.getTextChannelById(channel).sendMessage(
          eventsEmbed(
            "Events happening today",
            Color.decode("#42A5F5"),
            todayEvents
          )
        ).queue()
      }
    }
    Timer().schedule(postTask, time, TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
  }
}