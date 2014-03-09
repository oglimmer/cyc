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

6.) run "cyc_mgmt.sh -b -w -c -e" to build and deploy everything

7.) just start the web server. it will automatically start the engine process

(*) The management script "cyc_mgmt.sh" uses Tomcat's (>= 7.x) parallel deployment feature. 

(B) HOW TO INSTALL & DEPLOY VIA VAGRANT
---------------------------------------

1.)   install Vagrant and VirtualBox

2-a.) run deploy.sh in ./vagrant-multi-host to install it in a 3-VM configuration

2-b.) run deploy.sh in ./vagrant-single-host to install it in a single VM configuration

3.)   access the game via http://192.168.33.2
