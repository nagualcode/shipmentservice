#!/bin/bash

if [[ -z "$1" ]]; then
echo "Usage: $0 MICROSERVICE"
exit 1
fi

cd "$1" || exit 1
mvn clean install || exit 1
cd .. || exit 1
docker compose up -d --build "$1"



