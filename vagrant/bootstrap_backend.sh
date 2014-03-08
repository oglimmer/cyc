#!/usr/bin/env bash

# update the system
apt-get update
apt-get upgrade

# install java7
apt-get --yes install openjdk-7-jre sysv-rc-conf

# install the game-engine
mkdir /usr/local/cyc-engine
cp -r /vagrant/backend/* /usr/local/cyc-engine
cp /vagrant/cyc-engine /etc/init.d
chmod 755 /etc/init.d/cyc-engine

# auto start
sysv-rc-conf cyc-engine on

# create the property file
echo -e "couchdb.host=192.168.33.3" >>/etc/cyc.properties
echo -e "couchdb.user=user_cyc" >>/etc/cyc.properties
echo -e "couchdb.password=secretpassphrase" >>/etc/cyc.properties
echo -e "bind=0.0.0.0" >>/etc/cyc.properties

# start the engine
service cyc-engine start
