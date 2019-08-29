import commands.eventsEmbed
import configuration.botToken
import configuration.databaseUrl
import database.getRegisteredChannels
import database.setup
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
  setup(databaseUrl)
  println("Database setup")
  val jda = startBot(botToken) {
    configure {
      prefix = "!"
    }
  }.jda
  println("Bot setup")
  println("Starting daily post task")
  configureDailyPost(jda)
}

private fun configureDailyPost(jda: JDA) {
  with(Calendar.getInstance(TimeZone.getTimeZone(singaporeZone))) {
    set(Calendar.HOUR_OF_DAY, 10)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)

    println("Daily posts at ${this[Calendar.HOUR]}")

    val postTask = timerTask {
      println("Daily post")
      val todayEvents = meetup.findUpcomingEvents(20, listOf(292), singaporeDateTime)
      getRegisteredChannels().forEach {
        jda.getTextChannelById(it.channelId).sendMessage(
          eventsEmbed(
            "Events happening today",
            Color.decode("#42A5F5"),
            todayEvents
          )
        ).queue()
      }
    }
    Timer().scheduleAtFixedRate(postTask, time, TimeUnit.HOURS.toMillis(24))
  }
}