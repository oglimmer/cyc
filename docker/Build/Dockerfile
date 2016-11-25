FROM n3ziniuka5/ubuntu-oracle-jdk

MAINTAINER Oli Zimpasser <oglimmer@gmail.com>

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y apt-utils

RUN DEBIAN_FRONTEND=noninteractive apt-get install -y git maven npm && npm install -g jsdoc

WORKDIR /home

CMD git clone https://github.com/oglimmer/cyc && cd cyc && mvn package
