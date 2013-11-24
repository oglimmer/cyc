#!/bin/sh

usage="$(basename "$0") [-b] [-w v] [-e n] - builds or deploys cyc

where:
    -h  show this help text
    -b  build, performans a git pull and mvn package
    -w  war, deploy the war file with the given version n
    -e  engine, deploy the engine file with the given version n"

BUILD=
WAR=
ENGINE=

while getopts ':hbw:e:' option; do
  case "$option" in
    h) echo "$usage"
       exit
       ;;
    b) BUILD=YES
	     ;;
    w) WAR=$OPTARG
       ;;
    e) ENGINE=$OPTARG
       ;;
    :) printf "missing argument for -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1
       ;;
   \?) printf "illegal option: -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1
       ;;
  esac
done
shift $((OPTIND - 1))


if [ "$BUILD" == 'YES' ]; then
	git pull
	mvn clean package	
fi

if [ -n "$WAR" ]; then
	echo "Deploying war with version = $WAR"
	cp web/target/cyr##001.war /var/lib/tomcat/webapps/cyr##$WAR.war
fi

if [ -n "$ENGINE" ]; then
	echo "Deploying engine with version = $ENGINE"
	mkdir /usr/local/cyr-engine-container/tmp-deploy
	cp engine/target/engine-0.1-SNAPSHOT-jar-with-dependencies.jar /usr/local/cyr-engine-container/tmp-deploy
	mv /usr/local/cyr-engine-container/tmp-deploy /usr/local/cyr-engine-container/cyc$ENGINE
fi
