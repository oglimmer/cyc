#!/bin/sh

target_environment=$1
build_yml=$2
echo "Setting up $build_yml on $target_environment"

# create VM(s)
cd $target_environment
vagrant up
cd ..

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
../cyc_mgmt.sh -b -e -c -w

# copy couchDb scripts
mkdir -p $(pwd)/${0%/*}/roles/couchdb/files
cp -r ../persistence/src/couchdb/* $(pwd)/${0%/*}/roles/couchdb/files
echo "Deploying couchdb scripts to ./roles/couchdb/files"

# move the engine
mv $(pwd)/${0%/*}/roles/cyc-container/files/cyc-engine-container/cyc001 $(pwd)/${0%/*}/roles/cyc-engine/files/cyc-engine-container/

# prepare insecure ssh
rm -f /tmp/vagrant.key 
curl --silent https://raw.github.com/mitchellh/vagrant/master/keys/vagrant >/tmp/vagrant.key
chmod 400 /tmp/vagrant.key 
export ANSIBLE_HOST_KEY_CHECKING=False

# install ansible module 'cyc'
mkdir -p /usr/local/Cellar/ansible/1.5.0/share/ansible/oglimmer
cp ansible/cyc /usr/local/Cellar/ansible/1.5.0/share/ansible/oglimmer/cyc

# provision
ansible-playbook $build_yml.yml --user=vagrant --sudo --inventory-file=$target_environment/inventory --private-key=/tmp/vagrant.key
