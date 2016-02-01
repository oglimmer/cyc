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

#the engine shouldn't need properties. if you uncomment this, you need to add a permission:
#permission java.io.FilePermission "/etc/cyc.properties", "read";
#if [ -e "/etc/cyc.properties" ]; then
#	OPTS="$OPTS -Dcyc.properties=/etc/cyc.properties"
#fi

if [ -e "/var/lib/tomcat7/jmxtrans/jmxtrans-agent.jar" ]; then
	OPTS="$OPTS -javaagent:/var/lib/tomcat7/jmxtrans/jmxtrans-agent.jar=/usr/local/cyc-engine-container/jmxtrans.xml"
fi


$_java -Xmx768M $OPTS -XX:+UseConcMarkSweepGC -Djava.security.policy=/usr/local/cyc-engine-container/security.policy -Dcyc.home=/usr/local/cyc-engine-container -jar /usr/local/cyc-engine-container/engine-container-jar-with-dependencies.jar
