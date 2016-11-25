FROM n3ziniuka5/ubuntu-oracle-jdk

MAINTAINER Oli Zimpasser <oglimmer@gmail.com>

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y npm curl git maven netcat && npm install -g couchviews jsdoc && ln -s /usr/bin/nodejs /usr/bin/node

COPY entrypoint.sh /
COPY security.policy /usr/local/

ENTRYPOINT ["/entrypoint.sh"]
