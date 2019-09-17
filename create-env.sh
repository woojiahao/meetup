#!/bin/bash
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
read -p "Enter your bot token: " BOT_TOKEN

POSTGRES_DB="sgmeetupdiscord"
read -p "Enter your postgresql db name (sgmeetupdiscord) : " temp_postgres_db
if [[ ! -z $temp_postgres_db ]]; then
  POSTGRES_DB=$temp_postgres_db
fi

POSTGRES_USER="meetup"
read -p "Enter your postgresql db user (meetup) : " temp_postgres_user
if [[ ! -z $temp_postgres_user ]]; then
  POSTGRES_USER=$temp_postgres_user
fi

POSTGRES_PASSWORD="root"
read -p "Enter your postgresql db password (root) : " temp_postgres_password
if [[ ! -z $temp_postgres_password ]]; then
  POSTGRES_PASSWORD=$temp_postgres_password
fi

exit_if_blank "$BOT_TOKEN" "bot token"
exit_if_blank "$POSTGRES_DB" "postgresql db name"
exit_if_blank "$POSTGRES_USER" "postgresql user"
exit_if_blank "$POSTGRES_PASSWORD" "postgresql password"

DATABASE_URL="postgres://$POSTGRES_USER:$POSTGRES_PASSWORD@db:5432/$POSTGRES_DB"

touch $ENVIRONMENT_FILE_NAME

write_env "BOT_TOKEN" "$BOT_TOKEN"
write_env "POSTGRES_DB" "$POSTGRES_DB"
write_env "POSTGRES_USER" "$POSTGRES_USER"
write_env "POSTGRES_PASSWORD" "$POSTGRES_PASSWORD"
write_env "DATABASE_URL" "$DATABASE_URL"