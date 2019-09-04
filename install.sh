#!/bin/bash
read -p "Enter your application name: " applicationName
heroku create "$applicationName"

heroku addons:create heroku-postgresql:hobby-dev

read -p "Enter your database username provisioned by Heroku: " databaseUsername
heroku config:set DATABASE_USERNAME="$databaseUsername"

read -sp "Enter your database password provisioned by Heroku: " databasePassword
heroku config:set DATABASE_PASSWORD="$databasePassword"

read -sp "Enter your bot token: " botToken
heroku config:set BOT_TOKEN="$botToken"

heroku stack:set container

git push heroku master

heroku ps:scale worker=1