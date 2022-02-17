# Meetup
> Discord bot for getting tech event information in Singapore

## Deployment
You will need Docker installed on your machine and a bot token.

It is advised you use the scripts included to build your projects.

For any deployment, you must clone this repository (it is assumed this step is done before any other steps):

```bash
$ git clone https://github.com/woojiahao/meetup.git
$ cd meetup
```

Before any deployment, generate an `env_file.yml` with the `create-env.sh` script - feel free to leave the defaults if you are unsure and follow the prompts for the rest:

```bash
$ chmod +x create-env.sh
$ create-env.sh
```

The following file should be generated (follow this format if you are unable to run the script):

```
BOT_TOKEN=<bot_token>
POSTGRES_DB=sgmeetupdiscord
POSTGRES_USER=meetup
POSTGRES_PASSWORD=root
DATABASE_URL=postgres://meetup:root@db:5432/sgmeetupdiscord
```

### Stable
Stable deployment will be done if you wish to deploy the bot that's been tested without errors. The deployment will use the Docker image found on Docker Hub [here.](https://hub.docker.com/r/woojiahao/sg-meetup-discord) This is useful if you just want to deploy and forget about it.

1. Run `docker-compose.yml`
   
   ```bash
   $ docker-compose up
   ```

And that's all there is to deploying the stable build of the bot.

### Testing/Development
If you wish to make your own changes to the bot, you will want to see the changes made in real time, rather than having to deploy everything to Docker Hub.

1. Build the image from `docker-compose-local.yml`
   
   ```bash
   $ docker-compose -f docker-compose-local.yml build
   ```

2. Run `docker-compose-local.yml`
   
   ```bash
   $ docker-compose -f docker-compose-local.yml up
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

4. Add the Heroku Postgresql addon - the com.github.woojiahao.database is not used heavily, just to store some preferences.
   
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

## Libraries Used
1. [KUtils](https://gitlab.com/Aberrantfox/KUtils)
2. [khttp](https://khttp.readthedocs.io/en/latest/)
