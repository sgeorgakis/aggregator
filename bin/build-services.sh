#!/bin/sh

cd "$(dirname "$0")"

mkdir -p ../../lib
cp -r ../src/docker/ ../lib/docker/
cd ../lib/docker
docker-compose build
