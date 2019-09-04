package configuration

private fun getEnvironmentElseThrowException(environmentVariable: String, exceptionMessage: String) =
    System.getenv(environmentVariable) ?: throw IllegalStateException(exceptionMessage)

val botToken = getEnvironmentElseThrowException("BOT_TOKEN", "Bot token must be supplied as environment variable")
val databaseUrl = getEnvironmentElseThrowException("DATABASE_URL", "Heroku postgresql add on must be included")
val databaseUsername = getEnvironmentElseThrowException("DATABASE_USERNAME", "Get database username from Heroku site")
val databasePassword = getEnvironmentElseThrowException("DATABASE_PASSWORD", "Get database password from Heroku site")
