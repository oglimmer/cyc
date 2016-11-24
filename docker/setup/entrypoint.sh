#!/bin/bash

mkdir -p /home/build

cd /home/build
git clone https://github.com/oglimmer/cyc
cd cyc

#overwrite security.policy to allows multi host environment
cp -f /usr/local/security.policy ansible/roles/cyc-container/files/scripts/security.policy 

# create DB schema and views
curl -X PUT http://admin:secretpass@db:5984/cyc 
couchviews push http://admin:secretpass@db:5984/cyc persistence/src/couchdb

export CYC_ENGINE_CONTAINER=/usr/local/cyc-engine-container
export CYC_WEBAPPS=/opt/tomcat/webapps

./install.sh -l site
