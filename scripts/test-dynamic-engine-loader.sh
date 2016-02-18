#!/bin/sh

I=100

cd /usr/local/cyc-engine-container/

while :
do
	
	cp -r cyc001/ cyc$I

	sleep 5

	echo "test1" | nc localhost 9998

	sleep 3

	((I++))
done
