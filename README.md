# meetup
> Discord bot for getting tech event information in Singapore!

This bot is used in the Singapore computer science server, link [here.](https://discord.gg/RRZeV5A)

## Setup
### Heroku
1. Create a Discord bot account through the developer portal and obtain your bot token
2. Register for a Meetup.com account and obtain your API key
3. Add both tokens to the project as environment variables

    ```
    $ heroku config:set BOT_TOKEN=<discord_token>
    $ heroku config:set MEETUP_KEY=<meetup_api_key>
    ```

4. 

## Features
* [ ] Send upcoming events and today events automatically
* [ ] Display today events
* [ ] Integrate engineers.sg API