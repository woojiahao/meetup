# meetup
> Discord bot for getting event information in Singapore!

## Setup
### Heroku
1. Create a Discord bot account through the developer portal and obtain your bot token
2. Register for a Meetup.com account and obtain your API key
3. Add both tokens to the project as environment variables

    ```bash
    $ heroku config:set BOT_TOKEN=<discord_token>
    $ heroku config:set MEETUP_KEY=<meetup_api_key>
    ```