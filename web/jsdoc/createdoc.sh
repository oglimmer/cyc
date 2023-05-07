#!/bin/sh

jsdoc --help 1>/dev/null
if  [ $? -eq 0 ]; then
	npm install ink-docstrap taffydb
	rm -rf ../src/main/webapp/apidocs/* 
	jsdoc --verbose -d ../src/main/webapp/apidocs/ -c conf.json -t node_modules/ink-docstrap/template README.md .
fi

