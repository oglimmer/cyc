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

# create securit.policy for 3-host configuration
cp /vagrant/backend/security.policy /tmp/cyc-security.policy
sed 's/permission java.net.SocketPermission "127.0.0.1:-", "accept,resolve";/permission java.net.SocketPermission "192.168.33.2:-", "accept,resolve";/g' /tmp/cyc-security.policy  > /tmp/cyc-security.policy.1
sed 's/permission java.net.SocketPermission "localhost", "resolve";/permission java.net.SocketPermission "localhost", "resolve,listen";/g' /tmp/cyc-security.policy.1  > /tmp/cyc-security.policy.2
sed 's/permission java.net.SocketPermission "127.0.0.1:5984", "connect,resolve";/permission java.net.SocketPermission "192.168.33.3:5984", "connect,resolve";/g' /tmp/cyc-security.policy.2  > /tmp/cyc-security.policy.3
echo 'grant { permission java.io.FilePermission "/etc/cyc.properties", "read"; };' >> /tmp/cyc-security.policy.3
cp /tmp/cyc-security.policy.3 /etc/cyc-security.policy
rm /tmp/cyc-security.policy*

# auto start
sysv-rc-conf cyc-engine on

# create the property file
echo -e "couchdb.host=192.168.33.3" >>/etc/cyc.properties
echo -e "couchdb.user=user_cyc" >>/etc/cyc.properties
echo -e "couchdb.password=secretpassphrase" >>/etc/cyc.properties
echo -e "bind=0.0.0.0" >>/etc/cyc.properties

# start the engine
service cyc-engine start
