#!/bin/bash
docker run \
  --name localMQ \
  --env LICENSE=accept \
  --env MQ_QMGR_NAME=LQM1 \
  --volume qm1data:/mnt/mqm \
  --publish 1414:1414 \
  --publish 9443:9443 \
  --hostname qmgr \
  --env MQ_APP_PASSWORD=admin01 \
  -d \
  ibmcase/mq
