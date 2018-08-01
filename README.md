cyc (Code Your Company)
========================

LICENSE
-------

GNU LESSER GENERAL PUBLIC LICENSE V3

WHERE TO PLAY
-------------

The game is installed at <a href="https://codeyourrestaurant.com">codeyourrestaurant.com</a>. There are also customized rules in play to make the game even more fun ;)

(A) HOW TO INSTALL & DEPLOY ON A SINGLE LOCAL HOST
--------------------------------------------------

1.) install docker, curl, java, maven

2.) Optional: before you build the project you might want to edit the rules under rules/src/main/resources/*.groovy, but for testing purposes the default rules work as well

3.) run "install.sh -k site" to set up a local CouchDB and Tomcat, then build and deploy everything, finally all components are started

4.) browse to http://localhost:8080/cyr

(B) HOW TO INSTALL & DEPLOY VIA VAGRANT/ANSIBLE
-----------------------------------------------

1.) install Vagrant, VirtualBox and Ansible (and of course java,maven,npm for the build)

2.) execute "VAGRANT=UP install.sh -r [single-vm|multi-vm] site"

3.) browse http://192.168.66.2 for single-vm or http://192.168.66.3 for multi-vm

(C) HOW TO INSTALL & DEPLOY VIA DOCKER
--------------------------------------

1.) see docker directory (this is for demonostration purposes only)

Java -D variables
-----------------

* engine-container

	* cyc.debug = true|false, defines if a DebugEngineLoader or EngineLoader should be used by the engine-container
	* cyc.security = enabled|disable, defines Java runs with a SecurityManager instantiated
	* cyc.home = path to root of directory to engine-container/engine
	* java.security.policy = path to security.policy file. Included under ansible/roles/cyc-container/files/scripts/security.policy

* web

	* cyc.home =  <path> where the engine-container is installed. Can have a run.sh to start the engine-container
	* cyc.jmx = enable|disable, defines if the engine-container process should be called with remote JMX flags
	* cyc.remoteDebug = enable|disable, defines if the engine-container process should be called with remote debugging flags

* all

	* cyc.properties = <file> to a property file (format Java properties)

install.sh
----------

This script helps to install / update a local or remote installation. It supports Ansible provisioning on remote hosts, also it can set up a simple local environment and start all components after deployment.

It can also create release-tags on git.

Set CYC_ENGINE_CONTAINER to an empty directory of your choice.

Set CYC_WEBAPPS to the webapps directory of a Servlet 3.0 compliant web container. This script uses Tomcat's (>= 7.x) parallel deployment feature.

Set VAGRANT=UP if the remote command (-r) should automatically spin up a virtualbox VM via vagrant.