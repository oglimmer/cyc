#!/bin/bash

usage="$(basename "$0") gen|cp|rm - manages project specific id_rsa

where:
    -h  shows this help text
    gen generates a new key
    cp 	copies a key from ~/.ssh and removes the passphrase
    rm  removes this key"

cd ${0%/*}

while getopts ':h' option; do
  case "$option" in
    h) echo "$usage"
       exit
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

if [ "$1" != "gen" ] && [ "$1" != "cp" ] && [ "$1" != "rm" ]; then
   echo "$usage" >&2
   exit 1
fi

if [ "$1" = "gen" ]; then

	if [ -f ./ssh/id_rsa ]; then
		echo "An id_rsa key already exists."
		exit 1
	fi

	mkdir -p ./ssh
	ssh-keygen -t rsa -N "" -f ./ssh/id_rsa

fi

if [ "$1" = "cp" ]; then

	if [ -f ./ssh/id_rsa ]; then
		echo "An id_rsa key already exists."
		exit 1
	fi

	if [ ! -f ~/.ssh/id_rsa ]; then
		echo "You don't have a id_rsa file in ~/.ssh!"
		exit 1
	fi

	mkdir -p ./ssh
	cp ~/.ssh/id_rsa* ./ssh/

	echo "*** Removing the passphrase ..."
	ssh-keygen -p -f ssh/id_rsa -N ""

	touch ./ssh/copied-real-id_rsa

fi

if [ "$1" = "rm" ]; then
	rm -rf ./ssh
fi
