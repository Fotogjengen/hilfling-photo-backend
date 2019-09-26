#!/usr/bin/env bash

ssh -i ./id_rsa hilfling@$SERVER_IP_ADDRESS '
cd /var/www/hilfling-photo-backend &&
git fetch --all &&
git reset --hard origin/master &&
docker-compose -f ./docker-compose.yml -f ./docker-compose.deploy.yml up -d --build &&
LOGFILE="/var/www/hilfling-server/travis-deploys.log" &&
TIMESTAMP=`date "+%Y-%m-%d %H:%M:%S"` &&
echo $TIMESTAMP >> $LOGFILE &&
echo "DONE"
'
