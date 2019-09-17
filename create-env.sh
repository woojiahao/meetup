#!/bin/bash
# To add default values for prompts for miscellenous information
ENVIRONMENT_FILE_NAME="env_file.env"

exit_if_blank() {
  if [[ -z "$1" ]]; then
    echo "Cannot have empty $2"
    exit 1
  fi
}

write_env() {
  echo "$1=$2" >> $ENVIRONMENT_FILE_NAME
}

echo "Creating env_file.env for Docker secrets"
read -p "Enter your bot token :: " BOT_TOKEN
read -p "Enter your postgresql db name :: " POSTGRES_DB
read -p "Enter your postgresql db user :: " POSTGRES_USER
read -p "Enter your postgresql db password :: " POSTGRES_PASSWORD

exit_if_blank "$BOT_TOKEN" "bot token"
exit_if_blank "$POSTGRES_DB" "postgresql db name"
exit_if_blank "$POSTGRES_USER" "postgresql user"
exit_if_blank "$POSTGRES_PASSWORD" "postgresql password"

DATABASE_URL="postgres://$POSTGRES_USER:$POSTGRES_PASSWORD@localhost:5432/$POSTGRES_DB"

touch $ENVIRONMENT_FILE_NAME

write_env "BOT_TOKEN" "$BOT_TOKEN"
write_env "POSTGRES_DB" "$POSTGRES_DB"
write_env "POSTGRES_USER" "$POSTGRES_USER"
write_env "POSTGRES_PASSWORD" "$POSTGRES_PASSWORD"
write_env "DATABASE_URL" "$DATABASE_URL"