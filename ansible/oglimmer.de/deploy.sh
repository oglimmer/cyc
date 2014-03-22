#!/bin/sh

build_yml=$1
echo "Setting up $build_yml on oglimmer.de"
shift

# build and copy artifacts to roles/../files
cd ..
./build.sh

# prepare insecure ssh
#rm -f /tmp/vagrant.key 
#curl --silent https://raw.github.com/mitchellh/vagrant/master/keys/vagrant >/tmp/vagrant.key
#chmod 400 /tmp/vagrant.key 
#export ANSIBLE_HOST_KEY_CHECKING=False

# provision
ansible-playbook $build_yml.yml --user=vagrant --sudo --timeout=100 --inventory-file=oglimmer.de/inventory.ini --private-key=/tmp/vagrant.key --module-path modules $*
