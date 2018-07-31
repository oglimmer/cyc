FROM openjdk:8-jdk

MAINTAINER Oli Zimpasser <oglimmer@gmail.com>

RUN curl -sL https://deb.nodesource.com/setup_10.x | bash - && apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y apt-utils

RUN DEBIAN_FRONTEND=noninteractive apt-get install -y git maven nodejs && npm install -g jsdoc

WORKDIR /home

CMD git clone https://github.com/oglimmer/cyc && cd cyc && mvn package
