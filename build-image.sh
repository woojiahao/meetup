#!/bin/bash
# TODO: Remove dependency to Heroku Postgres for local run
# TODO: Automatically push to Docker Hub image
# TODO: Add line to docker-compose.yml - to change the image line with the bot line
read -p "Do you want to build the image? (y/N): " build_image_choice
if [[ $build_image_choice =~ ^[yY]$ ]]; then
  echo "Building Docker image"

  image_tag="sg-meetup-discord"
  read -p "Enter your tag (sg-meetup-discord): " temp_image_tag
  if [[ ! -z "$temp_image_tag" ]]; then
    image_tag=$temp_image_tag
  fi

  docker build -t "$image_tag" .
  docker image ls
fi