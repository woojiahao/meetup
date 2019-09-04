import configuration.botToken
import configuration.databasePassword
import configuration.databaseUrl
import configuration.databaseUsername
import database.setup
import me.aberrantfox.kjdautils.api.startBot

fun main() {
  setup(jdbcDatabaseUrl)
  startBot(botToken) { configure { prefix = "!" } }
}

private val jdbcDatabaseUrl
  get() = "jdbc:$databaseUrl?user=$databaseUsername&password=$databasePassword"