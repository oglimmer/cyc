#!/bin/bash

CMD="$1"

if [ "$CMD" == "clean" ]; then

	if docker ps -a|grep cyc-db; then
		docker rm -f cyc-db
	fi

	if docker ps -a|grep cyc-web; then
		docker rm -f cyc-web
	fi

	if docker ps -a|grep cyc-engine; then
		docker rm -f cyc-engine
	fi

	if docker network ls | grep -q isolated_nw; then
		docker network rm isolated_nw
	fi

	exit 0
fi

if ! docker network ls | grep -q isolated_nw; then
	docker network create -d bridge isolated_nw
fi

cd engine
./build.sh
cd ..

cd web
./build.sh
cd ..


echo "Starting db..."
docker run -d --name cyc-db --network=isolated_nw --network-alias=db -e COUCHDB_USER=admin -e COUCHDB_PASSWORD=secretpass couchdb

echo "Starting web..."
docker run -d --name cyc-web --network=isolated_nw --network-alias=web -p 8080:8080 codeyourrestaunt/web

echo "Starting engine..."
docker run -d --name cyc-engine --network=isolated_nw --network-alias=engine codeyourrestaunt/engine

cd setup
./build.sh
./run.sh
cd ..
