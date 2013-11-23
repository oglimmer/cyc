#!/bin/sh

mkdir -p target/dist
cp target/engine-container-0.1-SNAPSHOT-jar-with-dependencies.jar target/dist/
cp target/classes/security.policy target/dist/
mkdir -p target/dist/cyc0
cp ../engine/target/engine-0.1-SNAPSHOT-jar-with-dependencies.jar target/dist/cyc0
mkdir -p target/dist/logs
echo "java -Djava.security.policy=security.policy -Dcyc.home=.  -jar engine-container-0.1-SNAPSHOT-jar-with-dependencies.jar" >target/dist/run.sh
chmod 777 target/dist/run.sh