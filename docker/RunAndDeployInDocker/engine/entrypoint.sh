#!/bin/bash

while true; do

	if [ -f ./engine-container-jar-with-dependencies.jar ]; then
		./run.sh
	fi

	sleep 3

done
