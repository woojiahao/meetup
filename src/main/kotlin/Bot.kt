import configuration.botToken
import configuration.databaseUrl
import database.setup
import me.aberrantfox.kjdautils.api.startBot

fun main() {
  setup(databaseUrl)
  startBot(botToken) { configure { prefix = "!" } }
}