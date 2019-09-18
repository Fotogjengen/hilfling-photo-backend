#!/usr/bin/env bash
LOGFILE="/var/www/hilfling-server/travis-deploys.log"
TIMESTAMP=`date "+%Y-%m-%d %H:%M:%S"`
cd /var/www/hilfling-server/hilfling-photo-backend/
git reset --hard # TODO git clean -xdf? but that will remove node_modules and everything every time...
git pull
echo $TIMESTAMP >> $LOGFILE
