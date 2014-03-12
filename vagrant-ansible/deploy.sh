#!/bin/sh

target_environment=$1
echo "Setting up $target_environment"

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
mkdir ./roles/couchdb/files
cp -r ../persistence/src/couchdb/* ./roles/couchdb/files
echo "Deploying couchdb scripts to ./roles/couchdb/files"

# create VMs and provision apps
cd $target_environment
vagrant up
cd ..
sleep 5

# prepare insecure ssh
curl --silent https://raw.github.com/mitchellh/vagrant/master/keys/vagrant >/tmp/vagrant.key
chmod 400 /tmp/vagrant.key 
export ANSIBLE_HOST_KEY_CHECKING=False
# provision
ansible-playbook site.yml --user=vagrant --sudo --inventory-file=$target_environment/inventory --private-key=/tmp/vagrant.key
