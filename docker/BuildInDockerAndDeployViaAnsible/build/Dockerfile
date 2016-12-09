FROM ubuntu:16.04

MAINTAINER Oli Zimpasser <oglimmer@gmail.com>

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y apt-utils

RUN DEBIAN_FRONTEND=noninteractive apt-get install -y git npm curl git maven netcat python-pip openjdk-8-jdk ansible && npm install -g jsdoc couchviews && ln -s /usr/bin/nodejs /usr/bin/node && pip install --upgrade pip && pip install dopy six

WORKDIR "/home/build/cyc"

ENTRYPOINT ["./install.sh"]
