package configuration

val botToken = System.getenv("BOT_TOKEN")
    ?: throw IllegalStateException("Bot token must be supplied as BOT_TOKEN environment variable")
val databaseUrl = System.getenv("JDBC_DATABASE_URL")
    ?: throw java.lang.IllegalStateException("Heroku postgres add on must be configured")