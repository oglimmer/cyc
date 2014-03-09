#!/usr/bin/env bash

###
### 3rd pary setup
###

# update the system
apt-get update
apt-get upgrade

# install all required 3rd pary softeare
apt-get --yes install couchdb npm sysv-rc-conf tomcat7

# apply fix to Ubuntu 13.10 (http://richardhsu.net/2013/10/19/installing-nodejs-npm-on-ubuntu-13-10/)
ln -s /usr/bin/nodejs /usr/bin/node 
# install couchviews (import script)
npm install -g couchviews 


###
### couchdb setup
###

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


###
### cyc-game config file
###

# create the property file
echo -e "couchdb.user=user_cyc" >>/etc/cyc.properties
echo -e "couchdb.password=secretpassphrase" >>/etc/cyc.properties


###
### backend engine install & config
###

# install the game-engine
mkdir /usr/local/cyc-engine
cp -r /vagrant/backend/* /usr/local/cyc-engine
cp /vagrant/cyc-engine /etc/init.d
chmod 755 /etc/init.d/cyc-engine
# create security.policy for a config under /etc/cyc.properties
cp /vagrant/backend/security.policy /tmp/cyc-security.policy
sed 's/permission java.net.SocketPermission "localhost", "resolve";/permission java.net.SocketPermission "localhost", "resolve,listen";/g' /tmp/cyc-security.policy  > /tmp/cyc-security.policy.1
echo 'grant { permission java.io.FilePermission "/etc/cyc.properties", "read"; };' >> /tmp/cyc-security.policy.1
cp /tmp/cyc-security.policy.1 /etc/cyc-security.policy
rm /tmp/cyc-security.policy*
# auto start
sysv-rc-conf cyc-engine on
# start the engine
service cyc-engine start


###
### tomcat config and web-app
###

# copy server config
cp /vagrant/server.xml /etc/tomcat7/server.xml
# allow bind to port 80
sed 's/#AUTHBIND=no/AUTHBIND=yes/g' /etc/default/tomcat7 > /etc/default/tomcat7_mod
cp /etc/default/tomcat7_mod /etc/default/tomcat7
# pass game-engine property file to tomat
echo -e 'JAVA_OPTS="${JAVA_OPTS} -Dcyc.properties=/etc/cyc.properties"' >>/etc/default/tomcat7
service tomcat7 restart
# set redirect on /
echo -e "<%@ page contentType=\"text/html;charset=UTF-8\" language=\"java\" session=\"false\" %><% response.sendRedirect(\"/cyr\"); %>" >/var/lib/tomcat7/webapps/ROOT/index.jsp
rm /var/lib/tomcat7/webapps/ROOT/index.html
# copy game war
cp /vagrant/web/cyr##001.war /var/lib/tomcat7/webapps

