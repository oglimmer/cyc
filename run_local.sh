#!/bin/sh

trap cleanup 2

cleanup()
{
	echo "****************************************************************"
	echo "Stopping Docker, Tomcat and CYC-Container.....please wait...."
	echo "****************************************************************"
	docker rm -f $containerID
	./apache-tomcat-$TOMCAT_VERSION/bin/shutdown.sh
	exit 0
}

# vars
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
TOMCAT_VERSION=7.0.90
TOMCAT_URL=http://mirrors.ocf.berkeley.edu/apache/tomcat/tomcat-7/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz

# check for dependencies
mvn --version 1>/dev/null || exit 1
npm --version 1>/dev/null || exit 1
java -version 2>/dev/null || exit 1
jq --version 1>/dev/null || exit 1
curl --version 1>/dev/null || exit 1
docker --version 1>/dev/null || exit 1

#build
mvn package

# prepare env
mkdir -p target/localrun
cd target/localrun

# set up the couchdb views
containerID=$(docker run -d -p 5984:5984 couchdb:1.7)
if [ ! -d ./node_modules/couchviews ]; then
	npm install --no-package-lock --loglevel=error couchviews
fi
while [ "$(curl -s http://localhost:5984|jq -r '.couchdb')" != "Welcome" ]; do
	echo "waiting for couchdb..."
	sleep 1
done
curl -X PUT http://localhost:5984/cyc
./node_modules/.bin/couchviews push http://localhost:5984/cyc ../../persistence/src/couchdb/

# deploy the engine

mkdir -p cyc001/
cp ../../engine/target/engine-jar-with-dependencies.jar cyc001/


# ready the tomcat
if [ ! -d "./apache-tomcat-$TOMCAT_VERSION" ]; then
	curl -s $TOMCAT_URL | tar -xz
fi
cp ../../web/target/*.war apache-tomcat-$TOMCAT_VERSION/webapps/

# start tomcat
./apache-tomcat-$TOMCAT_VERSION/bin/startup.sh

# start engine-container (blocking)
cp ../../engine-container/target/engine-container-jar-with-dependencies.jar .
java -Dcyc.home=./ -Djava.security.policy=../../ansible/roles/cyc-container/files/scripts/security.policy -jar engine-container-jar-with-dependencies.jar
