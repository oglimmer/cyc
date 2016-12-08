#!/bin/sh

SSHUSER=root

export ANSIBLE_HOST_KEY_CHECKING=False

if [ ! -f ~/.ssh/id_rsa.pub ]; then
	echo "Faild to read ~/.ssh/id_rsa.pub"
	exit 1
fi

# create VM(s)
cd ansible/digital_ocean
ansible-playbook create_vm.yml --inventory-file=create_vm_inventory || exit 1
cd ../..
