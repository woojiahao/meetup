import configuration.botToken
import configuration.databaseUrl
import database.setup
import me.aberrantfox.kjdautils.api.startBot
import meetup.Meetup
import net.dv8tion.jda.core.JDA

private val meetup = Meetup()

fun main() {
  setup(databaseUrl)
  val jda = startBot(botToken) { configure { prefix = "!" } }.jda
//  configureDailyPost(jda)
}

private fun configureDailyPost(jda: JDA) {
//  with(Calendar.getInstance(TimeZone.getTimeZone(singaporeZone))) {
//    set(Calendar.HOUR_OF_DAY, 10)
//    set(Calendar.MINUTE, 0)
//    set(Calendar.SECOND, 0)
//
//    val postTask = timerTask {
//      val todayEvents = meetup.findUpcomingEvents(20, listOf(292), singaporeDateTime)
//      channels.forEach { channel ->
//        jda.getTextChannelById(channel).sendMessage(
//          eventsEmbed(
//            "Events happening today",
//            Color.decode("#42A5F5"),
//            todayEvents
//          )
//        ).queue()
//      }
//    }
//    Timer().schedule(postTask, time, TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
//  }
}