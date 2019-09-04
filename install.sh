#!/bin/bash
read -p "Enter your application name" applicationName
heroku create "$applicationName"
heroku addons:create heroku-postgresql:hobby-dev
read -p "Enter your bot token" botToken
heroku config:set BOT_TOKEN="$botToken"
heroku stack:set container
heroku push heroku master