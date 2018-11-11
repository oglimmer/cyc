module.exports = {

  config: {
    SchemaVersion: "1.0.0",
    Name: "cyc",
    Vagrant: {
      Box: "ubuntu/xenial64",
      Install: "maven openjdk-8-jdk-headless couchdb docker.io"
    }
  },

  software: {

    "cyc_build": {
      Source: "mvn",
      Artifact: "web/target/cyr##001.war"
    },

    "db": {
      Source: "couchdb",
      CouchDB: {
        Schema: "cyc",
        Create: [
          "persistence/src/couchdb/_design-GameRun-curl.json",
          "persistence/src/couchdb/_design-GameWinners-curl.json",
          "persistence/src/couchdb/_design-User-curl.json"
        ]
      }
    },

    "cyc_engine_container": {
      Source: "shell",
      Start: "$$TMP$$/cyc-engine-container/run.sh",
      ExposedPort: 9998,
      DockerImage: "openjdk",
      EnvVars: [
        "CYC_ENGINE_CONTAINER=$$TMP$$/cyc-engine-container"
      ],
      BeforeStart: [
        "CYC_ENG_CON_PATH=$$TMP$$/cyc-engine-container",
        "mkdir -p $CYC_ENG_CON_PATH",
        "cp engine-container/target/engine-container-jar-with-dependencies.jar $CYC_ENG_CON_PATH",
        "mkdir -p $CYC_ENG_CON_PATH/logs",
        "cp ansible/roles/cyc-container/files/scripts/security.policy $CYC_ENG_CON_PATH",
        "cp ansible/roles/cyc-container/files/scripts/run.sh $CYC_ENG_CON_PATH",
        "chmod 777 $CYC_ENG_CON_PATH/run.sh",
        "mkdir -p $CYC_ENG_CON_PATH/cyc001",
        "cp engine/target/engine-jar-with-dependencies.jar $CYC_ENG_CON_PATH/cyc001"
      ],
      config: {
        Name: "cyc_engine.properties",
        Connections: [ { Source:"db", Var: "couchdb.host" } ],
        Content: [ "bind=0.0.0.0" ],
        AttachAsEnvVar: ["OPTS", "-Dcyc.properties=$$SELF_NAME$$"]        
      }
    },

    "web": {
      Source: "tomcat",
      Deploy: "cyc_build",
      config: {
        Name: "cyc_web.properties",
        Connections: [ 
          { Source:"db", Var: "couchdb.host" },
          { Source:"cyc_engine_container", Var: "engine.host.full" },
          { Source:"cyc_engine_container", Var: "engine.host.test" }
        ],
        AttachAsEnvVar: ["JAVA_OPTS", "-Dcyc.properties=$$SELF_NAME$$"]        
      }
    }
  }
}
