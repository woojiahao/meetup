# Meetup
> Discord bot for getting tech event information in Singapore!

This bot is used in the Singapore computer science server, link [here.](https://discord.gg/RRZeV5A)

If you have any questions about this bot, feel free to approach me on the server - `@Chill#4048`.

## Setup
To run the bot either through Heroku or locally, you will need to have a bot account for Discord and obtain the bot token. Also ensure that you have Docker installed on your machine, I have Docker running on my work machine using Manjaro linux.

It is advised you use the scripts included to build your projects.

### Running locally
Running the Discord bot locally is ideal if you wish to host the bot on a different hosting provider. You can also use it for testing purposes where you wish to host it to contribute.

1. Clone this repository and navigate to project folder.
   
   ```bash
   $ git clone https://github.com/woojiahao/meetup.git
   $ cd meetup
   ```

2. If you are able to use shell scripts, it's advised you follow this step and the bot should be running on your local machine through Docker already, otherwise, skip to step 3 for a manual installation. With this script, you do not need to type long commands to build every time you make a change to the bot.
   
   ```bash
   $ chmod +x local-run.sh
   $ ./local-run.sh # Follow the prompts and everything should install accordingly
   ```

   **Note:** The database url is taken from Heroku if your project coincidentally is connected to Heroku already. If you don't have Heroku installed and wish to provide a database url, please supply the database url in the following format and the bot will handle the parsing to JDBC:

   ```
   postgres://<username>:<password>@<host>:<port>/<database>
   ```

3. Create a Docker image of the project.
   
   ```bash
   $ docker build -t <docker image name> .
   ```

4. Run the Docker image - supply 2 environment variables, `BOT_TOKEN` and `DATABASE_URL`. `DATABASE_URL` should follow the format above.
   
   ```bash
   $ docker run -e BOT_TOKEN=<bot token> -e DATABASE_URL=<database url> -d <docker image name>:latest
   ```

5. Check if the container is running.
   
   ```bash
   $ docker container ls
   ```

### Heroku
Installing the bot on Heroku is dead simple. Just make sure you have a Heroku account and you're logged into Heroku CLI.

To push changes to Heroku, simply perform a `git push heroku master`. 

1. Clone this repository and navigate to the project folder.
2. If you have access to shell scripts, use the provided script to reduce setup steps, otherwise, skip this step and move on to step 3.
   
   ```bash
   $ chmod +x heroku-run.sh
   $ ./heroku-run.sh
   ```

3. Create a Heroku application and ensure that you are connected to a Heroku remote Git repository.
   
   ```bash
   $ heroku create sg-meetup-discord
   $ git remote -v # There should be a remote named 'heroku'
   ```

4. Add the Heroku Postgresql addon - the database is not used heavily, just to store some preferences.
   
   ```bash
   $ heroku addons:create heroku-postgresql:hobby-dev
   ```

5. Configure an environment variable for the Discord bot token.
   
   ```bash
   $ heroku config:set BOT_TOKEN=<bot token>
   ```

6. Set the stack in Heroku to use a container - this has been explained in my demo project for Heroku + Docker + Discord bot found [here.](https://github.com/woojiahao/discord-docker)
   
   ```bash
   $ heroku stack:set container
   ```

7. Push the repository to Heroku. 
   
   ```bash
   $ git push heroku master
   ```

8. Scale the worker dyno so the bot is online.
   
   ```bash
   $ heroku ps:scale worker=1
   ```

## Upcoming Features
* [X] Send upcoming events and today events automatically
* [X] Display today events
* [X] Integrate engineers.sg API
* [X] Speed up Gradle builds
* [ ] Due to the nature of how KUtils work, JDA cannot be initialised at the start so either move away from KUtils or use a different library
* [ ] Write blog post about deploying Java apps on Docker - memory issue

## Libraries Used
1. [KUtils](https://gitlab.com/Aberrantfox/KUtils)
2. [khttp](https://khttp.readthedocs.io/en/latest/)
