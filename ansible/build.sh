#!/bin/sh

# clean transfer directories
rm -f $(pwd)/${0%/*}/roles/couchdb/files/*
rm -rf $(pwd)/${0%/*}/roles/cyc-container/files/cyc-engine-container/*
rm -rf $(pwd)/${0%/*}/roles/cyc-engine/files/cyc-engine-container/*
rm -f $(pwd)/${0%/*}/roles/tomcat7/files/*.war

# set build script variables
export CYC_WEBAPPS=$(pwd)/${0%/*}/roles/tomcat7/files
export CYC_ENGINE_CONTAINER=$(pwd)/${0%/*}/roles/cyc-container/files/cyc-engine-container

# mvn build needs more heap space
export MAVEN_OPTS="-Xmx3072m"

# build and copy/deploy files
../cyc_mgmt.sh -b -e -c -w || exit 1

# copy couchDb scripts
mkdir -p $(pwd)/${0%/*}/roles/couchdb/files
cp -r ../persistence/src/couchdb/* $(pwd)/${0%/*}/roles/couchdb/files
echo "Deploying couchdb scripts to ./roles/couchdb/files"

# move the engine
mkdir -p $(pwd)/${0%/*}/roles/cyc-engine/files/cyc-engine-container/
mv $(pwd)/${0%/*}/roles/cyc-container/files/cyc-engine-container/cyc001 $(pwd)/${0%/*}/roles/cyc-engine/files/cyc-engine-container/
