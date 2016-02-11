SETUP
-----

1.) install Vagrant and Ansible (and of course java,maven,npm for the build)

2.) execute deploy.sh [staging|production|oglimmer.de] [site|db|backend|backend-engine|web]

3.) browse http://192.168.66.2 for staging or http://192.168.66.3 for production


Java -D variables
-----------------

* engine-container

	cyc.debug = true|false, defines if a DebugEngineLoader or EngineLoader should be used by the engine-container
	cyc.security = enabled|disable, defines Java runs with a SecurityManager instantiated 

* web

	cyc.home =  <path> where the engine-container is installed. Can have a run.sh to start the engine-container  

* all

	cyc.properties = <file> to a property file (format Java properties)