#!/bin/bash
source ./env.sh

if [ -z "$1" ]; then
  echo 'running in normal...'
  mvn clean compile quarkus:dev
else
  echo 'running with extra arguments...'
  mvn clean compile "${@,1}" quarkus:dev
fi
