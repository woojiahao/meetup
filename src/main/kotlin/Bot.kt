import configuration.botToken
import me.aberrantfox.kjdautils.api.startBot

fun main() {
  startBot(botToken) {
    configure { prefix = "!" }
  }

  println("Bot up and running!")
}