#!/usr/bin/env bash

# update the system
apt-get update
apt-get upgrade

# install couchdb and node/npm (to import the views)
apt-get --yes install couchdb npm
# apply fix to Ubuntu 13.10 (http://richardhsu.net/2013/10/19/installing-nodejs-npm-on-ubuntu-13-10/)
ln -s /usr/bin/nodejs /usr/bin/node 
# install couchviews (import script)
npm install -g couchviews 

# create the couchdb database
curl --silent -X PUT http://127.0.0.1:5984/cyc

# import the views
sleep 1
# make sure no other files are in this directory (e.g. .DS_Store)
find /vagrant/db -not -name "*.json" -type f -delete
couchviews push http://127.0.0.1:5984/cyc /vagrant/db
sleep 1

# create an admin user
curl --silent -X PUT http://127.0.0.1:5984/_config/admins/user_cyc -d '"secretpassphrase"'

# bind on 0.0.0.0
sed 's/;bind_address = 127.0.0.1/bind_address = 0.0.0.0/g' /etc/couchdb/local.ini  > /etc/couchdb/local.ini_mod
cp /etc/couchdb/local.ini_mod /etc/couchdb/local.ini
service couchdb restart

