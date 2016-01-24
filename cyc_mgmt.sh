#!/bin/sh

usage="$(basename "$0") [-b|-a] [-w] [-c] [-e] - builds or deploys cyc

where:
    -h  shows this help text
    -b  build: performans a git pull and mvn package
    -a  auto build: builds/deploys only if there is a new commit in git
    -w  war: deploys the war file with a new version
    -c  engine-container: deploys the engine-container
    -e  engine: deploys the engine file with a new version"

cd ${0%/*}

if [ -z "$CYC_WEBAPPS" ]; then
  CYC_WEBAPPS=/var/lib/tomcat/webapps
fi
if [ -z "$CYC_ENGINE_CONTAINER" ]; then
  CYC_ENGINE_CONTAINER=/usr/local/cyc-engine-container
fi

while getopts ':hbweac' option; do
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
    c) ENGINE_CONTAINER=YES
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
  if [ "$git" != "Already up-to-date." ] && [ -n "$git" ]; then
    mvn clean package || exit 1    
  else
    WAR=
    ENGINE=
    ENGINE_CONTAINER=
  fi
fi

if [ -n "$BUILD" ]; then
  git pull
  mvn clean package || exit 1
fi

if [ -n "$WAR" ]; then
  for file in `ls -1 $CYC_WEBAPPS| grep cyr | sort -r| awk '{gsub(/\/.*\/|cyr##|.war/,"",$1); printf("%03d\n",++$1);}'`
  do
    VERSION=$file
    break
  done
  if [ -z "$VERSION" ]; then
    VERSION=001
  fi
  if [ -d "$CYC_WEBAPPS" ]; then
  	echo "Deploying war with version = $VERSION to $CYC_WEBAPPS"
  	cp web/target/cyr##001.war $CYC_WEBAPPS/cyr##$VERSION.war
  fi
fi

if [ -n "$ENGINE_CONTAINER" ]; then
	if [ -d "$CYC_ENGINE_CONTAINER" ]; then
		nc -z localhost 9998
		if [ $? -eq 0 ]; then
			echo "Deployment of engine-container aborted, since engine-container process is running"
			exit 1
		fi
	fi
	echo "Deploying engine-container to $CYC_ENGINE_CONTAINER"
	mkdir -p $CYC_ENGINE_CONTAINER
	cp engine-container/target/engine-container-jar-with-dependencies.jar $CYC_ENGINE_CONTAINER
	cp engine-container/target/classes/security.policy $CYC_ENGINE_CONTAINER	
	mkdir -p $CYC_ENGINE_CONTAINER/logs
	echo '#!/bin/bash\n\numask 0002\n\njava -Xmx256M -XX:MaxPermSize=256M -Djava.security.policy=security.policy -Dcyc.home=.  -jar engine-container-jar-with-dependencies.jar' >$CYC_ENGINE_CONTAINER/run.sh
	chmod 777 $CYC_ENGINE_CONTAINER/run.sh
fi

if [ -n "$ENGINE" ]; then
  for file in `ls -1 $CYC_ENGINE_CONTAINER | grep cyc | sort -r| awk '{gsub(/\/.*\/|cyc/,"",$1); printf("%03d\n",++$1);}'`
  do
    VERSION=$file
    break
  done
  if [ -z "$VERSION" ]; then
    VERSION=001
  fi
  if [ -d "$CYC_ENGINE_CONTAINER" ]; then
  	echo "Deploying engine with version = $VERSION to $CYC_ENGINE_CONTAINER"
  	mkdir $CYC_ENGINE_CONTAINER/tmp-deploy
  	cp engine/target/engine-jar-with-dependencies.jar $CYC_ENGINE_CONTAINER/tmp-deploy
  	mv $CYC_ENGINE_CONTAINER/tmp-deploy $CYC_ENGINE_CONTAINER/cyc$VERSION
  fi
fi


