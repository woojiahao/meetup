import configuration.botToken
import me.aberrantfox.kjdautils.api.startBot

fun main() {
  val jda = startBot(botToken) {
    configure { prefix = "!" }
  }

  println("Bot up and running!")
}