#!/bin/bash

if [ -z "$1" ]; then
	echo "you need to give a parameter = number of users to create"
	exit 1
fi

START=1
END=$1

for (( c=$START; c<=$END; c++ ))
do

	NEW_UUID=$(openssl rand -base64 8 |md5 |head -c8;echo)

	sed s/UNAME/$NEW_UUID/g new-user.json >new-user-tmp.json

	curl -s -X PUT -d @new-user-tmp.json localhost:5984/cyc/$NEW_UUID 1>/dev/null

	echo "created user $NEW_UUID"

done

