#!/bin/bash

# check for digital ocean API key
if [ -z "$DO_API_TOKEN" ]; then
	echo "You need to set DO_API_TOKEN"
	exit 1
fi

# set up local environment

mkdir -p ./repos
mkdir -p ./.m2

if [ ! -d ./ssh ]; then
	echo "You don't have id_rsa in this project. Use key-manage.sh!"
	exit 1
fi

cd ./repos
if [ ! -d cyc ]; then
	git clone https://github.com/oglimmer/cyc
else 
	cd cyc && git fetch && git pull && cd ..
fi
cd ..

# build docker image

docker pull ubuntu:16.04
docker build -t codeyourrestaurant/buildvm build

# run build within docker container

docker run --rm -v $PWD/ssh:/root/.ssh -v $PWD/repos:/home/build -v $PWD/.m2:/root/.m2 -e DO_API_TOKEN=$DO_API_TOKEN codeyourrestaurant/buildvm "$@"

if [ -f ./ssh/copied-real-id_rsa ]; then
	echo "**********************************************************"
	echo "* THIS PROJECT STORES A REAL ID_RSA WITHOUT A PASSPHRASE *"
	echo "**********************************************************"
fi
