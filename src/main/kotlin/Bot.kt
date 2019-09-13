import api.EngineersSGAPI
import com.github.woojiahao.commands.scheduleDailyUpdate
import configuration.botToken
import configuration.databaseUrl
import database.getDailyPostTiming
import database.setup
import me.aberrantfox.kjdautils.api.startBot
import net.dv8tion.jda.api.JDA
import java.util.concurrent.ScheduledExecutorService

var dailyPostService: ScheduledExecutorService? = null
val engineersSGAPI = EngineersSGAPI()

fun main() {
  setup(databaseUrl)

  val kUtils = startBot(botToken) {
    configure {
      prefix = "!"
      globalPath = "com.github.woojiahao"
    }
  }

  with(kUtils.discord.jda) {
    awaitReady()
    loadDailyPost(this)
  }
}

private fun loadDailyPost(jda: JDA) {
  val dailyPostTiming = getDailyPostTiming() ?: return
  println(dailyPostTiming)
  scheduleDailyUpdate(dailyPostTiming, jda)
}