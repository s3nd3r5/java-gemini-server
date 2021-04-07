FROM openjdk:11

RUN mkdir /opt/jgs/
WORKDIR /opt/jgs/
EXPOSE 1965

VOLUME /etc/certs
VOLUME /var/log/jgs
VOLUME /var/gemini

COPY docker-entrypoint.sh ./
COPY server.jar ./
COPY logback.xml ./
COPY lib  ./lib
COPY gemini-server.conf ./

ENTRYPOINT /opt/jgs/docker-entrypoint.sh
