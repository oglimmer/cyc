#!/bin/sh

# clean transfer directories
rm -rf $(pwd)/${0%/*}/web/*
rm -rf $(pwd)/${0%/*}/backend/*
rm -rf $(pwd)/${0%/*}/db/*

# set build script variables
export CYC_WEBAPPS=$(pwd)/${0%/*}/web
export CYC_ENGINE_CONTAINER=$(pwd)/${0%/*}/backend

# build and copy/deploy files
../cyc_mgmt.sh -b -e -c -w

# copy couchDb scripts
cp -r ../persistence/src/couchdb/* ./db
echo "Deploying couchdb scripts to ./db"

# create VMs and provision apps
vagrant up

