#!/bin/sh

build_yml=$1
echo "Setting up $build_yml on oglimmer.de"
shift

# build and copy artifacts to roles/../files
cd ..
./build.sh

VAGRANT=no
if [ "$VAGRANT" = "yes" ]; then
	# prepare insecure ssh
	rm -f /tmp/vagrant.key 
	curl --silent https://raw.github.com/mitchellh/vagrant/master/keys/vagrant >/tmp/vagrant.key
	chmod 400 /tmp/vagrant.key 
	export ANSIBLE_HOST_KEY_CHECKING=False
	USER=vagrant
	SUDO="--sudo"
	SSHKEY="--private-key=/tmp/vagrant.key"
else 
	USER=root
fi

# provision
ansible-playbook $build_yml.yml --user=$USER $SUDO --timeout=100 --inventory-file=oglimmer.de/inventory.ini $SSHKEY --module-path modules $*
