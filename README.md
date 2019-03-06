# meetup
> Discord bot for getting tech event information in Singapore!

This bot is used in the Singapore computer science server, link [here.](https://discord.gg/RRZeV5A)

## Setup
### Local
If you plan to contribute to the bot, you can run it locally and make changes.

1. Fork and clone this repository

    ```bash
    $ git clone https://github.com/woojiahao/meetup
    ```

2. Create a Discord bot account through the developer portal and obtain your bot token
3. Register for a Meetup.com account and obtain your API key
4. Depending on your IDE of choice, you must set up the environment variables used.

    1. [Intellij](https://stackoverflow.com/questions/13748784/setting-up-and-using-environmental-variables-in-intellij-idea)
    2. [Netbeans](https://stackoverflow.com/questions/11823233/how-to-set-environment-variable-in-netbeans)
    3. [Eclipse](https://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.cdt.doc.user%2Ftasks%2Fcdt_t_run_env.htm)

5. Run the `main` function in `Bot.kt` 

### Heroku
If you plan to host the bot on your own server, follow these steps to get setup.

1. Fork and clone this repository

    ```bash
    $ git clone https://github.com/woojiahao/meetup
    ```

2. Create a Discord bot account through the developer portal and obtain your bot token
3. Register for a Meetup.com account and obtain your API key
4. Prepare the repository for Heroku

    ```bash
    $ heroku create
    ```

4. Configure the bot tokens on Heroku

    ```
    $ heroku config:set BOT_TOKEN=<discord_token>
    $ heroku config:set MEETUP_API_KEY=<meetup_api_key>
    $ heroku config:set GRADLE_TASK="shadowJar"
    $ heroku config:set BOT_CHANNEL_<server> =<target_channel_id>
    ```

5. Push the project to the Heroku remote

    ```bash
    $ git push heroku master
    ```

6. Deploy the worker dyno for the bot

    ```bash
    $ heroku ps:scale worker=1
    ```
    
## Upcoming Features
* [ ] Send upcoming events and today events automatically
* [X] Display today events
* [ ] Integrate engineers.sg API

## Libraries Used
1. [KUtils](https://gitlab.com/Aberrantfox/KUtils)
2. [khttp](https://khttp.readthedocs.io/en/latest/)