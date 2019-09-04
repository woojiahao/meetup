package configuration

class DatabaseInformation(username: String, password: String, host: String, port: Int, database: String) {
  private val databaseUrl = "jdbc:postgres://$host:$port/$database?user=$username&password=$password"
  override fun toString() = databaseUrl
}

private fun getEnvironmentElseThrowException(environmentVariable: String, exceptionMessage: String) =
    System.getenv(environmentVariable) ?: throw IllegalStateException(exceptionMessage)

private fun getDatabaseInformation(databaseUrl: String): DatabaseInformation {
  val databaseUrlRegex = Regex("^postgres://([\\w\\d]+):([\\w\\d]+)@([\\w\\d-.]+):([\\d]+)/([\\w\\d]+)\$")
  val matchResult = databaseUrlRegex.matchEntire(databaseUrl)
      ?: throw IllegalStateException("Database string is in improper format")
  with(matchResult.groups) {
    val username = get(1)!!.value
    val password = get(2)!!.value
    val host = get(3)!!.value
    val port = get(4)!!.value.toInt()
    val database = get(5)!!.value
    return DatabaseInformation(username, password, host, port, database)
  }
}

val botToken = getEnvironmentElseThrowException("BOT_TOKEN", "Bot token must be supplied as environment variable")
val databaseUrl = getDatabaseInformation(
  getEnvironmentElseThrowException(
    "DATABASE_URL",
    "Heroku postgresql add on must be included"
  )
).toString()
