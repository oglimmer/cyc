cyc (Code Your Company)
========================

LICENSE
-------

GNU LESSER GENERAL PUBLIC LICENSE V3

WHERE TO PLAY
-------------

The game is installed at <a href="http://cyr.oglimmer.de">cyr.oglimmer.de</a>. There are also customized rules in play to make the game even more fun ;)

(A) HOW TO INSTALL & DEPLOY ON A SINGLE HOST
--------------------------------------------

1.) install CouchDB on localhost:5984 and create a database called "cyc"

2.) load the views under persistence/src/couchdb/*.json into into localhost:5984/cyc (https://npmjs.org/package/couchviews does the trick easily)

3.) before you build the project you might want to edit the rules under rules/src/main/resources/*.groovy (but for testing purposes the default rules work as well)

4.) set CYC_ENGINE_CONTAINER to an empty directory of your choice

5.) set CYC_WEBAPPS to the webapps directory of a Servlet 3.0 compliant web container (*). Also add -Dcyc.home=$CYC_ENGINE_CONTAINER as JVM parameter

6.) run "install.sh -l site" to build and deploy everything

7.) just start the web server. it will automatically start the engine process

(*) The management script "install.sh" uses Tomcat's (>= 7.x) parallel deployment feature. 

(B) HOW TO INSTALL & DEPLOY VIA VAGRANT/ANSIBLE
-----------------------------------------------

1.) install Vagrant and Ansible (and of course java,maven,npm for the build)

2.) execute install.sh -r [single-vm|multi-vm] site

3.) browse http://192.168.66.2 for single-vm or http://192.168.66.3 for multi-vm


Java -D variables
-----------------

* engine-container

	cyc.debug = true|false, defines if a DebugEngineLoader or EngineLoader should be used by the engine-container
	cyc.security = enabled|disable, defines Java runs with a SecurityManager instantiated 

* web

	cyc.home =  <path> where the engine-container is installed. Can have a run.sh to start the engine-container
	cyc.jmx = enable|disable, defines if the engine-container process should be called with remote JMX flags
	cyc.remoteDebug = enable|disable, defines if the engine-container process should be called with remote debugging flags

* all

	cyc.properties = <file> to a property file (format Java properties)
