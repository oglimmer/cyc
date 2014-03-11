#!/bin/sh

# clean transfer directories
rm -f $(pwd)/${0%/*}/roles/couchdb/files/*
rm -rf $(pwd)/${0%/*}/roles/cyc-engine/files/cyc-engine/*
rm -f $(pwd)/${0%/*}/roles/tomcat7/files/*.war

# set build script variables
export CYC_WEBAPPS=$(pwd)/${0%/*}/roles/tomcat7/files
export CYC_ENGINE_CONTAINER=$(pwd)/${0%/*}/roles/cyc-engine/files/cyc-engine

# mvn build needs more heap space
export MAVEN_OPTS="-Xmx3072m"

# build and copy/deploy files
../cyc_mgmt.sh -b -e -c -w

# copy couchDb scripts
cp -r ../persistence/src/couchdb/* ./roles/couchdb/files
echo "Deploying couchdb scripts to ./roles/couchdb/files"

# create VMs and provision apps
vagrant up

# add server cert to known_hosts
cat known_hosts >>~/.ssh/known_hosts

# provision
ansible-playbook play.yml --sudo