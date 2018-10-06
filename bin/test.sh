#!/bin/sh

cd "$(dirname "$0")"
cd ../src
mvn test
