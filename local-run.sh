#!/bin/bash
docker build -t sg-meetup-discord .
docker image ls

if [[ -z "$BOT_TOKEN" ]]; then
  read -p "Enter your bot token: " token
  echo "Saving bot token :: $token"
  export BOT_TOKEN="$token"
  echo "Bot token stored as :: $BOT_TOKEN"
else
  echo "Bot token to use :: $BOT_TOKEN"
fi

if [[ $(git ls-remote --exit-code heroku) = 0 ]]; then
  read -p "Enter your database url (postgres://<username>:<password>@<host>:<port>/<database>): " database
else
  database=$(heroku config:get DATABASE_URL)
fi

docker run -e BOT_TOKEN="$BOT_TOKEN" -e DATABASE_URL="$database" -d sg-meetup-discord:latest
docker container ls