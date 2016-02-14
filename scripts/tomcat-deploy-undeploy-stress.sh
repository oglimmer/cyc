#!/bin/bash

USER="scr:scr"

while :
do
	
	curl -u $USER -T ../web/target/cyr##001.war 'http://localhost:8080/manager/text/deploy?path=/cyr&update=true'

	sleep 3

	curl -s 'http://localhost:8080/cyr/Landing.action' | grep -q restaurant

	if [ "$?" -ne 0 ]; then
		echo "retrievel of page failed."
	fi

	sleep 3

	curl -u $USER 'http://localhost:8080/manager/text/undeploy?path=/cyr'

	sleep 3

done


