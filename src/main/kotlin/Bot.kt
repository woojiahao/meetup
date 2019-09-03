import configuration.botToken
import configuration.databaseUrl
import database.setup
import me.aberrantfox.kjdautils.api.startBot

fun main() {
  setup(databaseUrl)
  println("Database setup")
  startBot(botToken) { configure { prefix = "!" } }
  println("Bot setup")
  println("Starting daily post task")
}