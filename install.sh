#!/bin/bash
set_env() {
  heroku config:set "$1"="$2"
}

prompt_env_then_set() {
  read -p "$1" variable
  set_env "$2" "$variable"
}

read -p "Enter your application name: " applicationName
heroku create "$applicationName"

heroku addons:create heroku-postgresql:hobby-dev

prompt_env_then_set "Enter your bot token: " "BOT_TOKEN"

heroku stack:set container

git push heroku master

heroku ps:scale worker=1