#!/bin/bash

if [ x"$#" != x"1" ];then
    echo "Usage: $0 BROKER_MM_NAME"
    exit -1
fi

BROKER_MM_NAME="$1"
NAME="rocketmq-$BROKER_MM_NAME"
PUID="1000"
PGID="1000"

VOLUME="$HOME/docker_data/rocketmq-$BROKER_MM_NAME"
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
    --env MM_BROKER_ID=$BROKER_MM_NAME \
    --env PUID=$PUID \
    --env PGID=$PGID \
    --env NAMESRV_ADDR="rocketmq-ns:9876" \
    -p 10911:10911 \
    -p 10909:10909 \
    --network nw \
    --detach \
    --restart always \
    coder4/rocketmq:4.3.2 sh mqbroker 

