#!/bin/bash

# check for digital ocean API key
if [ -z "$DO_API_TOKEN" ]; then
	echo "You need to set DO_API_TOKEN"
fi

# set up local environment

mkdir -p ./repos
mkdir -p ./.m2

if [ ! -d ./ssh ]; then
	mkdir -p ./ssh
	ssh-keygen -t rsa -N "" -f ./ssh/id_rsa
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

docker run --rm -v $PWD/ssh:/root/.ssh -v $PWD/repos:/home/build -v $PWD/.m2:/root/.m2 -e DO_API_TOKEN=$DO_API_TOKEN codeyourrestaurant/buildvm