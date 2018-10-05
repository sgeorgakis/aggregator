#!/bin/sh

cd "$(dirname "$0")"

cd ../lib/docker
docker-compose up
