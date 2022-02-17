import api.EngineersSGAPI
import commands.scheduleDailyUpdate
import configuration.botToken
import configuration.databaseUrl
import database.getDailyPostTiming
import database.setup
import dev.kord.core.Kord
import me.jakejmattson.discordkt.api.dsl.bot
import java.util.concurrent.ScheduledExecutorService

var dailyPostService: ScheduledExecutorService? = null
val engineersSGAPI = EngineersSGAPI()

fun main() {
  setup(databaseUrl)

  bot(botToken) {
    prefix{ "-" }

    configure {
      allowMentionPrefix = true
      showStartupLog = true
      recommendCommands = true
    }

    presence {
      listening("--help")
    }

    onStart {
      loadDailyPost(kord)
    }
  }
}

private fun loadDailyPost(kord: Kord) {
  val dailyPostTiming = getDailyPostTiming() ?: return
  println(dailyPostTiming)
  scheduleDailyUpdate(dailyPostTiming, kord)
}