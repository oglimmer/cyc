# ==============================================================
# Multi-stage Dockerfile for CodeYourRestaurant
# Produces two runtime images: "web" and "engine"
# ==============================================================

# --------------------------------------------------------------
# Stage 1: Maven build (shared by web + engine)
# --------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-11 AS build

WORKDIR /build
COPY pom.xml .
COPY engine-api/pom.xml engine-api/
COPY engine/pom.xml engine/
COPY engine-container/pom.xml engine-container/
COPY web/pom.xml web/
COPY rules/pom.xml rules/
COPY persistence/pom.xml persistence/

# Download dependencies first (cached layer)
RUN mvn dependency:go-offline -B -pl '!web' || true

COPY . .
RUN mvn -B -DskipTests package

# --------------------------------------------------------------
# Stage 2: Web (Tomcat 9 + WAR)
# --------------------------------------------------------------
FROM tomcat:9-jdk11-temurin AS web

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /build/web/target/cyr##001.war /usr/local/tomcat/webapps/cyr##001.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

# --------------------------------------------------------------
# Stage 3: Engine (JDK 8 + engine jars)
# --------------------------------------------------------------
FROM eclipse-temurin:8-jre AS engine

RUN mkdir -p /opt/cyc-engine-container/cyc001 /opt/cyc-engine-container/logs

COPY --from=build /build/engine-container/target/engine-container-jar-with-dependencies.jar /opt/cyc-engine-container/
COPY --from=build /build/engine/target/engine-jar-with-dependencies.jar /opt/cyc-engine-container/cyc001/
COPY ansible/roles/cyc-container/files/scripts/security.policy /opt/cyc-engine-container/
# Patch security policy for Docker networking:
# 1. Allow CouchDB connections to any host (not just 127.0.0.1)
# 2. Allow accepting connections from any host (not just 127.0.0.1)
RUN sed -i 's|permission java.net.SocketPermission "127.0.0.1:5984", "connect,resolve";|permission java.net.SocketPermission "*:5984", "connect,resolve";|' \
    /opt/cyc-engine-container/security.policy && \
    echo '' >> /opt/cyc-engine-container/security.policy && \
    echo 'grant { permission java.net.SocketPermission "*:*", "accept,resolve"; };' >> /opt/cyc-engine-container/security.policy
COPY ansible/roles/cyc-container/files/scripts/run.sh /opt/cyc-engine-container/
RUN chmod +x /opt/cyc-engine-container/run.sh

ENV CYC_ENGINE_CONTAINER=/opt/cyc-engine-container

EXPOSE 9998

CMD ["/opt/cyc-engine-container/run.sh"]
