#!/bin/bash

umask 0002

java -Xmx256M -XX:MaxPermSize=256M -Djava.security.policy=/etc/cyc-security.policy -Dcyc.home=/usr/local/cyc-engine-container -Dcyc.properties=/etc/cyc.properties  -jar /usr/local/cyc-engine-container/engine-container-jar-with-dependencies.jar
