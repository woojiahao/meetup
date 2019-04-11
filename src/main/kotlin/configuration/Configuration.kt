package configuration

val botToken = System.getenv("BOT_TOKEN")
    ?: throw IllegalStateException("Bot token must be supplied as BOT_TOKEN environment variable")
val meetupApiToken = System.getenv("MEETUP_API_KEY")
    ?: throw IllegalStateException("Meetup api key must be supplied as MEETUP_API_KEY environment variable")
val databaseUrl = System.getenv("JDBC_DATABASE_URL")
    ?: throw java.lang.IllegalStateException("Heroku postgres add on must be configured")