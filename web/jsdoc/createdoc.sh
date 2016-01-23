#!/bin/sh

jsdoc -v 1>/dev/null
if  [ $? -eq 0 ]; then
	rm -rf ../src/main/webapp/apidocs/* 
	jsdoc --verbose -d ../src/main/webapp/apidocs/ -c conf.json -t node_modules/ink-docstrap/template README.md .
fi

