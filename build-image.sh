#!/bin/bash
image_tag="sg-meetup-discord"
read -p "Enter your tag (sg-meetup-discord): " temp_image_tag
if [[ ! -z "$temp_image_tag" ]]; then
  image_tag=$temp_image_tag
fi

docker build -t "$image_tag" .
docker image ls