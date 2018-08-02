#!/bin/bash

#
# SECTION: FUNCTIONS
#

function cleanTransferDirectories() 
{
		rm -f $(pwd)/${0%/*}/ansible/roles/couchdb/files/*
		rm -rf $(pwd)/${0%/*}/ansible/roles/cyc-container/files/cyc-engine-container/*
		rm -rf $(pwd)/${0%/*}/ansible/roles/cyc-engine/files/cyc-engine-container/*
		rm -f $(pwd)/${0%/*}/ansible/roles/tomcat7/files/*.war	
}

#
# SECTION: HELP / USAGE
#

usage="$(basename "$0") [-c] [-t] [-v] [-s] [-l|-k|-r remote-environment] type - deploys and/or releases cyc

where:
    -h  shows this help text
    -t  tag: created a new TAG in git and uses this tag
    -l  local: builds and deploys to local
    -k  all local: set ups infrastructure, builds, deploys and runs
    -r  remote: builds and deploys to a remote host
    -v  verbose: enables verbose output
    -s  skip build: for -l and -r skips the build
    -c  clean: cleans all build/temporary files

    type = site|web|backend|backend-engine"

#
# SECTION: RESOLVE PARAMETER
#

cd ${0%/*}

while getopts ':htlkr:vsc' option; do
  PARAM_GIVEN=YES
  case "$option" in
    h) echo "$usage"
       exit
       ;;
    t) CREATE_TAG=YES
       ;;
    l) LOCAL_BUILD=YES
       ;;
    k) LOCAL_BUILD=YES
	   LOCAL_INFRA_SETUP=YES
	   ;;
    r) REMOTE_BUILD=YES
	   REMOTE_ENVIRONMENT_NAME=$OPTARG
	   if [ -z "$REMOTE_ENVIRONMENT_NAME" ]; then
	   		echo "Missing remote-environment"
	   		exit 1
	   fi
       ;;
    v) VERBOSE="-vvvv"
	   ;;
	s) SKIP_BUILD=YES
	   ;;
	c) CLEAN=YES
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
TYPE_PARAM="$1"

if [ -z "$PARAM_GIVEN" ]; then
   echo "$usage" >&2
   exit 1
fi

#
# SECTION: COMMAND EXECUTION
#

if [ "$CLEAN" == "YES" ] && [ "$SKIP_BUILD" == "YES" ]; then
	echo "Illegal to use clean and skip build together. Install aborted."
	exit 1
fi

if [ "$CLEAN" == "YES" ]; then
	mvn clean
	cleanTransferDirectories
	rm -rf ansible/single-vm/.vagrant
	rm -rf ansible/multi-vm/.vagrant
	rm -rf localrun
fi

if [ -z "$TYPE_PARAM" ]; then
	echo "Missing type. Install aborted."
	exit 1
fi


if [ "$CREATE_TAG" == "YES" ]; then

	git checkout master && git fetch && git pull

	LAST_MESSAGE=$(git log --format=%B -n 1)
	$(echo $LAST_MESSAGE | grep -q "\[maven-release-plugin\] prepare for next development iteration")
	LAST_MESSAGE_C1=$?
	$(echo $LAST_MESSAGE | grep -q "\[maven-release-plugin\] prepare release cyc-")
	LAST_MESSAGE_C2=$?

	if [ $LAST_MESSAGE_C1 -eq 0 -o $LAST_MESSAGE_C2 -eq 0 ]; then
		echo "No commits since last tag. No build needed."
		exit 1
	fi

	LATEST_MINOR_VERSION=$(git describe --abbrev=0 --tags | grep -o "[0-9]*$")
	NEW_MINOR_VERSION=$((LATEST_MINOR_VERSION + 1))
	NEW_MINOR_VERSION_PLUS_ONE=$((NEW_MINOR_VERSION + 1))

	TAG=cyc-0.$NEW_MINOR_VERSION
	RELEASE=0.$NEW_MINOR_VERSION
	DEV=0.$NEW_MINOR_VERSION_PLUS_ONE-SNAPSHOT

	echo "********************************************************************"
	echo "Create new release with:"
	echo "TAG=$TAG"
	echo "RELEASE=$RELEASE"
	echo "DEV=$DEV"

	mvn --batch-mode -Dtag=$TAG -DreleaseVersion=$RELEASE -DdevelopmentVersion=$DEV release:prepare release:clean || exit 1

	git checkout tags/$TAG

fi


if [ "$LOCAL_BUILD" == "YES" ]; then

	if [ "$SKIP_BUILD" != "YES" ]; then
		mvn clean package || exit 1
	fi

	if [ "$LOCAL_INFRA_SETUP" == "YES" ]; then

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
		TOMCAT_VERSION=7.0.90
		TOMCAT_URL=http://mirrors.ocf.berkeley.edu/apache/tomcat/tomcat-7/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz
		CYC_WEBAPPS=./localrun/apache-tomcat-$TOMCAT_VERSION/webapps
		CYC_ENGINE_CONTAINER=./localrun

		# check for dependencies
		curl --version 1>/dev/null || exit 1
		docker --version 1>/dev/null || exit 1

		# prepare env
		mkdir -p localrun
		cd localrun

		# set up couchdb and its views
		containerID=$(docker run -d -p 5984:5984 couchdb:1.7)
		while [ "$(curl --write-out %{http_code} --silent --output /dev/null http://localhost:5984)" != "200" ]; do
			echo "waiting for couchdb..."
			sleep 1
		done
		curl -X PUT http://localhost:5984/cyc
		curl -X POST -H "Content-Type: application/json" -d @../persistence/src/couchdb/_design-GameRun-curl.json http://localhost:5984/cyc
		curl -X POST -H "Content-Type: application/json" -d @../persistence/src/couchdb/_design-GameWinners-curl.json http://localhost:5984/cyc
		curl -X POST -H "Content-Type: application/json" -d @../persistence/src/couchdb/_design-User-curl.json http://localhost:5984/cyc

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
	fi

	if [ "$TYPE_PARAM" == "web" ] || [ "$TYPE_PARAM" == "site" ]; then

		if [ -z "$CYC_WEBAPPS" ]; then
		  CYC_WEBAPPS=/opt/tomcat7/webapps
		  echo "Defaulting to webapp to $CYC_WEBAPPS"
		fi

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

	if [ "$TYPE_PARAM" == "backend" ] || [ "$TYPE_PARAM" == "backend-engine" ] || [ "$TYPE_PARAM" == "site" ]; then

		if [ -z "$CYC_ENGINE_CONTAINER" ]; then
		  CYC_ENGINE_CONTAINER=/usr/local/cyc-engine-container
		  echo "Defaulting to backend to $CYC_ENGINE_CONTAINER"
		fi	

		if [ "$TYPE_PARAM" == "backend" ] || [ "$TYPE_PARAM" == "site" ]; then
			if [ -d "$CYC_ENGINE_CONTAINER" ]; then
				nc -z localhost 9998
				if [ $? -eq 0  ] && [ -z $IGNORE_RUNNING_SYSTEM ]; then
					echo "Deployment of engine-container aborted, since engine-container process is running"
					exit 1
				fi
			fi
			echo "Deploying engine-container to $CYC_ENGINE_CONTAINER"
			mkdir -p $CYC_ENGINE_CONTAINER
			cp engine-container/target/engine-container-jar-with-dependencies.jar $CYC_ENGINE_CONTAINER			
			mkdir -p $CYC_ENGINE_CONTAINER/logs
			cp ansible/roles/cyc-container/files/scripts/run.sh $CYC_ENGINE_CONTAINER
			if [ -f ansible/roles/cyc-container/files/scripts/jmxtrans.xml ]; then
				cp ansible/roles/cyc-container/files/scripts/jmxtrans.xml $CYC_ENGINE_CONTAINER
			fi
			cp ansible/roles/cyc-container/files/scripts/security.policy $CYC_ENGINE_CONTAINER	
			chmod 777 $CYC_ENGINE_CONTAINER/run.sh
		fi


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


	if [ "$LOCAL_INFRA_SETUP" == "YES" ]; then
			# start tomcat
		$CYC_WEBAPPS/../bin/startup.sh

		# start engine-container (blocking)
		CYC_ENGINE_CONTAINER=$CYC_ENGINE_CONTAINER $CYC_ENGINE_CONTAINER/run.sh		
	fi


elif [ "$REMOTE_BUILD" == "YES" ]; then

	. ansible/$REMOTE_ENVIRONMENT_NAME/settings.sh

	if [ "$VAGRANT" = "YES" ] || [ "$VAGRANT" = "UP" ]; then
		export ANSIBLE_HOST_KEY_CHECKING=False		
	fi

	if [ "$VAGRANT" = "UP" ]; then
		# create VM(s)
		cd ansible/$REMOTE_ENVIRONMENT_NAME
		vagrant up
		cd ../..		
	fi

	if [ "$SKIP_BUILD" != "YES" ]; then

		cleanTransferDirectories

		# mvn build needs more heap space
		export MAVEN_OPTS="-Xmx3072m"

		mvn clean package || exit 1
		
		mkdir -p $(pwd)/${0%/*}/ansible/roles/cyc-container/files/cyc-engine-container
		mkdir -p $(pwd)/${0%/*}/ansible/roles/cyc-engine/files/cyc-engine-container/cyc001

		cp web/target/cyr##001.war $(pwd)/${0%/*}/ansible/roles/tomcat7/files
		cp engine-container/target/engine-container-jar-with-dependencies.jar $(pwd)/${0%/*}/ansible/roles/cyc-container/files/cyc-engine-container/		
		cp engine/target/engine-jar-with-dependencies.jar $(pwd)/${0%/*}/ansible/roles/cyc-engine/files/cyc-engine-container/cyc001/

		# copy couchDb scripts
		mkdir -p $(pwd)/${0%/*}/ansible/roles/couchdb/files
		cp -r persistence/src/couchdb/* $(pwd)/${0%/*}/ansible/roles/couchdb/files		

	fi

	echo "Using TOMCAT_MANAGER_PASSWORD=$TOMCAT_MANAGER_PASSWORD"

	# provision
	cd ansible

	ansible-playbook $TYPE_PARAM.yml --user=$SSHUSER $SSHSUDO --timeout=100 \
		--inventory-file=$REMOTE_ENVIRONMENT_NAME/inventory.ini $SSHKEY  --module-path modules $VERBOSE

fi





if [ "$CREATE_TAG" == "YES" ]; then

	# some clean-up
	git checkout master

fi
