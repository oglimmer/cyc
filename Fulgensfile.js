module.exports = {

  config: {
    SchemaVersion: "1.0.0",
    Name: "cyc",
    Vagrant: {
      Box: "ubuntu/xenial64",
      Install: "maven openjdk-8-jdk-headless couchdb docker.io"
    }
  },

  versions: {
    build: {
      TestedWith: "3-jdk-11"
    },
    db: {
      TestedWith: "1.7 & 2"
    },
    engine: {
      JavaLocal: "1.8",
      Docker: "8-jre",
      KnownMax: "Java 8"
    },
    tomcat: {
      Docker: "tomcat9-openjdk11-openj9",
      TestedWith: "7 & 9"
    }
  },

  software: {

    build: {
      Source: "mvn",
      Artifact: "web/target/cyr##001.war"
    },

    db: {
      Source: "couchdb",
      //DockerImage: "oglimmer/pouchdb",
      DockerMemory: "200M",
      CouchDB: {
        Schema: "cyc",
        Create: [
          "persistence/src/couchdb/_design-GameRun-curl.json",
          "persistence/src/couchdb/_design-GameWinners-curl.json",
          "persistence/src/couchdb/_design-User-curl.json"
        ]
      }
    },

    engine: {
      Source: "shell",
      Start: "$$TMP$$/cyc-engine-container/run.sh",
      ExposedPort: 9998,
      DockerImage: "openjdk",
      DockerMemory: "200M",
      EnvVars: [
        { Name: "CYC_ENGINE_CONTAINER", Value: "$$TMP$$/cyc-engine-container" },
        { Name: "OPTS", Value: "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap" }
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
        Content: [
          { Line: "bind=0.0.0.0"},
          { Line: "couchdb.host=$$VALUE$$", Source:"db" }
        ],
        AttachAsEnvVar: ["OPTS", "-Dcyc.properties=$$SELF_NAME$$"]        
      },
      securityConfig: {
        Name: "security.policy",
        Content: [
          { Line: "grant { permission java.net.SocketPermission \"*:*\", \"accept,resolve\"; };" },
          {
            Source: "db",
            Regexp: "permission java.net.SocketPermission \"127.0.0.1:5984\", \"connect,resolve\";",
            Line: "permission java.net.SocketPermission \"$$VALUE$$:5984\", \"connect,resolve\";"
          }
        ],
        LoadDefaultContent: "ansible/roles/cyc-container/files/scripts/security.policy",
        AttachIntoDockerAsFile: "/home/node/exec_env/localrun/cyc-engine-container/security.policy"
      }
    },

    tomcat: {
      Source: "tomcat",
      DockerImage: "oglimmer/adoptopenjdk-tomcat",
      DockerMemory: "120M",
      Deploy: "build",
      config: {
        Name: "cyc_web.properties",
        Content: [ 
          { Source:"db", Line: "couchdb.host=$$VALUE$$" },
          { Source:"engine", Line: "engine.host.full=$$VALUE$$" },
          { Source:"engine", Line: "engine.host.test=$$VALUE$$" }
        ],
        AttachAsEnvVar: ["JAVA_OPTS", "-Dcyc.properties=$$SELF_NAME$$"]        
      }
    }
  }
}
