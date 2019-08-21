#!/bin/bash

if [ $# -eq 2 ]
then
  containerID=$1
  hostn=$2
else
  if [ $# -eq 1 ]
  then
    containerID=$1   
  else
    echo "Usage $0 containerID [hostname]"
    exit 1
  fi
  hostn="localhost:9080"
fi
url="http://$hostn/containers/"


sed "s/C100/${containerID}/g" container.json > new.json
curl -X POST -H "Content-Type: application/json" -d "@./new.json" $url
rm new.json