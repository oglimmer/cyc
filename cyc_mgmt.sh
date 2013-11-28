#!/bin/sh

usage="$(basename "$0") [-b|-a] [-w] [-e] - builds or deploys cyc

where:
    -h  shows this help text
    -b  build: performans a git pull and mvn package
    -a  auto build: builds/deploys only if there is a new commit in git
    -w  war: deploys the war file with a new version
    -e  engine: deploys the engine file with a new version"

if [ -z "$CYC_WEBAPPS" ]; then
  CYC_WEBAPPS=/var/lib/tomcat/webapps
fi
if [ -z "$CYC_ENGINE_CONTAINER" ]; then
  ENGINE_CONTAINER=/usr/local/cyr-engine-container
fi

while getopts ':hbwea' option; do
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
    a) AUTO_BUILD=YES
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

if [ -n "$AUTO_BUILD" ]; then
  BUILD=
  git=$(git pull)
  if [ "$git" != "Already up-to-date." ]; then
    mvn clean package 
  else
    WAR=
    ENGINE=
  fi
fi

if [ -n "$BUILD" ]; then
  git pull
  mvn clean package 
fi

if [ -n "$WAR" ]; then
  for file in `ls -1 $CYC_WEBAPPS/cyr* | sort -r| awk '{gsub(/\/.*\/|cyr##|.war/,"",$1); printf("%03d\n",++$1);}'`
  do
    VERSION=$file
    break
  done
  if [ -z "$VERSION" ]; then
    VERSION=001
  fi
  echo "Deploying war with version = $VERSION"
  cp web/target/cyr##001.war $CYC_WEBAPPS/cyr##$VERSION.war
fi

if [ -n "$ENGINE" ]; then
  for file in `ls -1 $CYC_ENGINE_CONTAINER/cyc* | sort -r| awk '{gsub(/\/.*\/|cyc##/,"",$1); printf("%03d\n",++$1);}'`
  do
    VERSION=$file
    break
  done
  if [ -z "$VERSION" ]; then
    VERSION=001
  fi
  echo "Deploying engine with version = $VERSION"
  mkdir $CYC_ENGINE_CONTAINER/tmp-deploy
  cp engine/target/engine-jar-with-dependencies.jar $CYC_ENGINE_CONTAINER/tmp-deploy
  mv $CYC_ENGINE_CONTAINER/tmp-deploy $CYC_ENGINE_CONTAINER/cyc$VERSION
fi
