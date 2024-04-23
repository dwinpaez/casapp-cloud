#!/bin/bash/env bash
set -e

if [ -z "$1" ]; then
  ENV=local
else
  ENV=$1
fi

DEBUG=false

echo "==> Environment Profile $ENV"

DOCKER_ENV="\
  --env PORT=8081 \
  --env DB_DATABASE=casapp_clients \
  --env DB_USERNAME=casapp_user \
  --env DB_PASSWORD=casapp_pwd_2024 \
  --env SPRING_PROFILE=$ENV \
  --env BOOKINGS_SERVICE_URL=msvc-bookings:8002/booking"

if [ $DEBUG ]; then
  sudo docker run --rm --name msvc-clients -p 8081:8081 $DOCKER_ENV --network casapp-net msvc-clients
else
  sudo docker run --detach --rm --name msvc-clients -p 8081:8081 $DOCKER_ENV --network casapp-net msvc-clients > /dev/null 2>&1
fi