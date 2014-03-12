#!/bin/sh

CYC_WEBAPPS=/var/lib/tomcat7/webapps

for file in `ls -1 $CYC_WEBAPPS| grep cyr | sort -r| awk '{gsub(/\/.*\/|cyr##|.war/,"",$1); printf("%03d\n",++$1);}'`
do
	VERSION=$file
	break
done

if [ -z "$VERSION" ]; then
	VERSION=001
fi

cp /tmp/cyr##001.war $CYC_WEBAPPS/cyr##$VERSION.war
