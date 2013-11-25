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

while getopts ':hbwe' option; do
  case "$option" in
    h) echo "$usage"
       exit
       ;;
    b) BUILD=YES
       ;;
    w) WAR=YES
       ;;
    e) ENGINE=YES
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


if [ -n "$BUILD" ]; then
  git pull
  mvn clean package 
fi

if [ -n "$WAR" ]; then
  for file in `ls -1 /var/lib/tomcat/webapps/cyr*.war | sort -r| awk '{gsub(/\/.*\/|cyr##|.war/,"",$1); printf("%03d\n",++$1);}'`
  do
    VERSION=$file
    break
  done
  echo "Deploying war with version = $VERSION"
  cp web/target/cyr##001.war /var/lib/tomcat/webapps/cyr##$VERSION.war
fi

if [ -n "$ENGINE" ]; then
        for file in `ls -1 /usr/local/cyr-engine-container/cyc* | sort -r| awk '{gsub(/\/.*\/|cyc##/,"",$1); printf("%03d\n",++$1);}'`
        do
                VERSION=$file
                break
        done
  echo "Deploying engine with version = $VERSION"
  mkdir /usr/local/cyr-engine-container/tmp-deploy
  cp engine/target/engine-0.1-SNAPSHOT-jar-with-dependencies.jar /usr/local/cyr-engine-container/tmp-deploy
  mv /usr/local/cyr-engine-container/tmp-deploy /usr/local/cyr-engine-container/cyc$VERSION
fi
