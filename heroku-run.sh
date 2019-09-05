#!/bin/bash
read -p "Enter your application name: " applicationName
echo "Creating Heroku application"
heroku create "$applicationName"

echo "Adding Heroku Postgresql addon"
heroku addons:create heroku-postgresql:hobby-dev

read -p "Enter your bot token: " token
heroku config:set BOT_TOKEN="$token"

echo "Setting Heroku stack to Docker container"
heroku stack:set container

git push heroku master

echo "Enabling Discord bot"
heroku ps:scale worker=1