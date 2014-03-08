#!/usr/bin/env bash


# update the system
apt-get update
apt-get upgrade

# install tomcat-7
apt-get --yes install tomcat7

# copy server config
cp /vagrant/server.xml /etc/tomcat7/server.xml
# allow bind to port 80
sed 's/#AUTHBIND=no/AUTHBIND=yes/g' /etc/default/tomcat7 > /etc/default/tomcat7_mod
cp /etc/default/tomcat7_mod /etc/default/tomcat7
# pass game-engine property file to tomat
echo -e 'JAVA_OPTS="${JAVA_OPTS} -Dcyc.properties=/etc/cyc.properties"' >>/etc/default/tomcat7
service tomcat7 restart

# create the property file
echo -e "engine.host=192.168.33.4" >>/etc/cyc.properties
echo -e "couchdb.host=192.168.33.3" >>/etc/cyc.properties
echo -e "couchdb.user=user_cyc" >>/etc/cyc.properties
echo -e "couchdb.password=secretpassphrase" >>/etc/cyc.properties

# set redirect on /
echo -e "<%@ page contentType=\"text/html;charset=UTF-8\" language=\"java\" session=\"false\" %><% response.sendRedirect(\"/cyr\"); %>" >/var/lib/tomcat7/webapps/ROOT/index.jsp
rm /var/lib/tomcat7/webapps/ROOT/index.html

# copy game war
cp /vagrant/web/cyr##001.war /var/lib/tomcat7/webapps

