FROM openjdk:8-jdk-alpine

MAINTAINER Oli Zimpasser <oglimmer@gmail.com>

RUN apk update && apk upgrade && apk add git

RUN MAVEN_VERSION=3.3.3 \
 && cd /usr/share \
 && wget -q http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz -O - | tar xzf - \
 && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
 && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven

WORKDIR /home

CMD git clone https://github.com/oglimmer/cyc && cd cyc && mvn package
