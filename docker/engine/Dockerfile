FROM ingensi/oracle-jdk

MAINTAINER Oli Zimpasser <oglimmer@gmail.com>

COPY cyc.properties /etc/

WORKDIR /usr/local/cyc-engine-container

VOLUME /usr/local/cyc-engine-container

COPY entrypoint.sh /usr/local/cyc-engine-container

CMD ["/usr/local/cyc-engine-container/entrypoint.sh"]
