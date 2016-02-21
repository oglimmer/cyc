#!/bin/bash

umask 0002

if type -p java 1>/dev/null; then
    #echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    #echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "no java"
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    #echo version "$version"
    if [[ "$version" > "1.8" ]]; then
    	OPTS="$OPTS"
        #echo version is more than 1.8
    else         
    	OPTS="$OPTS -XX:MaxPermSize=256M"
        #echo version is less than 1.8
    fi
fi

if [ -e "/etc/cyc.properties" ]; then
	OPTS="$OPTS -Dcyc.properties=/etc/cyc.properties"
fi

if [ -e "/var/lib/tomcat7/jmxtrans/jmxtrans-agent.jar" ]; then
	OPTS="$OPTS -javaagent:/var/lib/tomcat7/jmxtrans/jmxtrans-agent.jar=/usr/local/cyc-engine-container/jmxtrans.xml"
fi

if free >/dev/null 2>&1; then
	TOTAL_MEM=$(free|grep Mem|awk '{print $2}')
	if [ $TOTAL_MEM -lt 600000 ]; then
	    XMX=128M
	elif [ $TOTAL_MEM -lt 1100000 ]; then
	    XMX=384M
	elif [ $TOTAL_MEM -lt 1600000 ]; then
	    XMX=512M
	else
	    XMX=640M
	fi
else
	XMX=512M
fi

$_java -Xms$XMX -Xmx$XMX $OPTS -XX:+UseConcMarkSweepGC -Djava.security.policy=/usr/local/cyc-engine-container/security.policy \
    -Dcyc.home=/usr/local/cyc-engine-container -jar /usr/local/cyc-engine-container/engine-container-jar-with-dependencies.jar
