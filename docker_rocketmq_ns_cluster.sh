#!/bin/bash

NAME="rocketmq-ns"
PUID="1000"
PGID="1000"

VOLUME="$HOME/docker_data/rocketmq-ns"
VOLUME_LOGS="$VOLUME/logs"
VOLUME_STORE="$VOLUME/store"
mkdir -p $VOLUME_LOGS 
mkdir -p $VOLUME_STORE

docker ps -q -a --filter "name=$NAME" | xargs -I {} docker rm -f {}
docker run \
    --hostname $NAME \
    --name $NAME \
    --volume "$VOLUME_LOGS":/root/logs \
    --volume "$VOLUME_STORE":/root/store \
    --env MAX_POSSIBLE_HEAP=8589934592 \
    --env PUID=$PUID \
    --env PGID=$PGID \
    -p 9876:9876 \
    --network host \
    --detach \
    --restart always \
    coder4/rocketmq:4.3.2 sh mqnamesrv

