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

docker ps -q -a --filter "name=$NAME" | xargs -I {} docker rm -f {}
docker run \
    --hostname $NAME \
    --name $NAME \
    --volume "$VOLUME_LOGS":/root/logs \
    --volume "$VOLUME_STORE":/root/store \
    --env MM_BROKER_ID=$BROKER_MM_NAME \
    --env PUID=$PUID \
    --env PGID=$PGID \
    --env NAMESRV_ADDR="192.168.6.85:9876" \
    -p 10911:10911 \
    -p 10909:10909 \
    --network host \
    --detach \
    --restart always \
    coder4/rocketmq:4.3.2 sh mqbroker 

