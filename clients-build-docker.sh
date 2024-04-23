#!/bin/bash/env bash
set -e

if [ -z "$1" ]; then
  ENV=dev
else
  ENV=$1
fi

sudo docker build -t msvc-clients:latest . -f msvc-clients/Dockerfile