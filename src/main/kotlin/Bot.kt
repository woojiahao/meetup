import configuration.botToken
import configuration.databaseUrl
import database.setup
import me.aberrantfox.kjdautils.api.startBot

fun main() {
  setup(databaseUrl)

  val kUtils = startBot(botToken) {
    configure {
      prefix = "!"
      globalPath = "com.github.woojiahao"
    }
  }

  kUtils.discord.jda.awaitReady()
}

