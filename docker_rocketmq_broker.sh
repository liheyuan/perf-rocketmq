#!/bin/bash

NAME="rocketmq-broker"
PUID="1000"
PGID="1000"

VOLUME="$HOME/docker_data/rocketmq-broker"
VOLUME_LOGS="$VOLUME/logs"
VOLUME_STORE="$VOLUME/store"
mkdir -p $VOLUME_LOGS 
mkdir -p $VOLUME_STORE

docker network create nw
docker ps -q -a --filter "name=$NAME" | xargs -I {} docker rm -f {}
docker run \
    --hostname $NAME \
    --name $NAME \
    --volume "$VOLUME_LOGS":/root/logs \
    --volume "$VOLUME_STORE":/root/store \
    --env MAX_POSSIBLE_HEAP=8589934592 \
    --env PUID=$PUID \
    --env PGID=$PGID \
    --env NAMESRV_ADDR="rocketmq-ns:9876" \
    -p 10911:10911 \
    -p 10909:10909 \
    --network nw \
    --detach \
    --restart always \
    rocketmq:latest sh mqbroker 

