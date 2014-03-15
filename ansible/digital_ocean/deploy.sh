#!/bin/sh

build_yml=$1
echo "Setting up $build_yml on digital ocean"

# create VM(s)
ansible-playbook create_vm.yml --inventory-file=localhost
cd $(pwd)/${0%/*}/..

# build and copy artifacts to roles/../files
./build.sh

# prepare insecure ssh
export ANSIBLE_HOST_KEY_CHECKING=False

# provision
ansible-playbook $build_yml.yml --user=root --inventory-file=digital_ocean/digitalocean --module-path modules
