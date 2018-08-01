#!/bin/sh

trap cleanup 2

cleanup()
{
	echo "****************************************************************"
	echo "Stopping CouchDB, Tomcat and CYC-Container.....please wait...."
	echo "****************************************************************"
	docker rm -f $containerID
	$CYC_WEBAPPS/../bin/shutdown.sh
	exit 0
}

# vars
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
TOMCAT_VERSION=7.0.90
TOMCAT_URL=http://mirrors.ocf.berkeley.edu/apache/tomcat/tomcat-7/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz
CYC_WEBAPPS=./localrun/apache-tomcat-$TOMCAT_VERSION/webapps
CYC_ENGINE_CONTAINER=./localrun

# check for dependencies
npm --version 1>/dev/null || exit 1
jq --version 1>/dev/null || exit 1
curl --version 1>/dev/null || exit 1
docker --version 1>/dev/null || exit 1

# prepare env
mkdir -p localrun
cd localrun

# set up couchdb and its views
containerID=$(docker run -d -p 5984:5984 couchdb:1.7)
if [ ! -d ./node_modules/couchviews ]; then
	npm install --no-package-lock --loglevel=error couchviews
fi
while [ "$(curl -s http://localhost:5984|jq -r '.couchdb')" != "Welcome" ]; do
	echo "waiting for couchdb..."
	sleep 1
done
curl -X PUT http://localhost:5984/cyc
./node_modules/.bin/couchviews push http://localhost:5984/cyc ../persistence/src/couchdb/

# download tomcat
if [ ! -f "/$TMPDIR/apache-tomcat-$TOMCAT_VERSION.tar" ]; then
	curl -s $TOMCAT_URL | gzip -d >/$TMPDIR/apache-tomcat-$TOMCAT_VERSION.tar
fi
# extract tomcat
if [ ! -d "./apache-tomcat-$TOMCAT_VERSION" ]; then
	tar -xf /$TMPDIR/apache-tomcat-$TOMCAT_VERSION.tar -C ./
fi

# build and install via install.sh
cd ../
./install.sh -l site

# start tomcat
$CYC_WEBAPPS/../bin/startup.sh

# start engine-container (blocking)
$CYC_ENGINE_CONTAINER/run.sh
