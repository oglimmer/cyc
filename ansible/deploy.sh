#!/bin/sh

usage="$(basename "$0") [-s] target-environment yaml-target

where:
    -h  shows this help text
    -s  skip build"

while getopts ':hs' option; do
  case "$option" in
    h) echo "$usage"
       exit
       ;;
    s) SKIP_BUILD=YES
       ;;
    :) printf "missing argument for -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1
       ;;
   \?) printf "illegal option: -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1
       ;;
  esac
done
shift $((OPTIND - 1))

target_environment=$1
build_yml=$2
if [ "$target_environment" == "" ]; then
	echo "no environment given!"
	exit 1
fi
if [ "$build_yml" == "" ]; then
	echo "no yaml target given!"
	exit 1
fi
echo "Setting up $build_yml on $target_environment"
shift
shift

. $target_environment/settings.sh

if [ "$VAGRANT" = "YES" ]; then
	# create VM(s)
	cd $target_environment
	vagrant up
	cd ..
fi

if [ "$SKIP_BUILD" != "YES" ]; then
	# build and copy artifacts to roles/../files
	./build.sh
fi

if [ "$VAGRANT" = "YES" ]; then
	# prepare insecure ssh
	rm -f /tmp/vagrant.key 
	curl --silent https://raw.github.com/mitchellh/vagrant/master/keys/vagrant >/tmp/vagrant.key
	chmod 400 /tmp/vagrant.key 
	export ANSIBLE_HOST_KEY_CHECKING=False
fi

echo "Using TOMCAT_MANAGER_PASSWORD=$TOMCAT_MANAGER_PASSWORD"

# provision
ansible-playbook $build_yml.yml --user=vagrant --sudo --timeout=100 --inventory-file=$target_environment/inventory.ini --private-key=/tmp/vagrant.key --module-path modules $*
