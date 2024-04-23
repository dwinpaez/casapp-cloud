#!/bin/bash/env bash
set -e

if [ -z "$1" ]; then
  ENV=local
else
  ENV=$1
fi
java -Dspring.profiles.active=$ENV -jar target/msvc-clients-0.0.1-SNAPSHOT.jar ca.casapp.springcloud.msvc.clients.MsvcClientsApplication