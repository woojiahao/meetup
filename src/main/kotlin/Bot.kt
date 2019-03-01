import me.aberrantfox.kjdautils.api.startBot

fun main() {
  val token = System.getenv("BOT_TOKEN")

  startBot(token) {
    configure { prefix = "!" }
  }

  println("Bot up and running!")
}