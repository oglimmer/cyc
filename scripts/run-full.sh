#!/bin/sh

COUNTER=0
while [  $COUNTER -lt 10 ]; do

#	sleep 6

	echo "full"|nc localhost 9998

#	sleep 10

	let COUNTER=COUNTER+1 

done

