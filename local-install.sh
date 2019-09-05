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

docker run -e BOT_TOKEN="$BOT_TOKEN" -e DATABASE_URL="$(heroku config:get DATABASE_URL)" -d sg-meetup-discord:latest
docker container ls