#!/bin/sh

SSHUSER=root

export ANSIBLE_HOST_KEY_CHECKING=False

# create VM(s)
cd ansible/digital_ocean
ansible-playbook create_vm.yml --inventory-file=create_vm_inventory || exit 1
cd ../..
