FROM java:11

RUN mkdir /opt/jgs/
WORKDIR /opt/jgs/
EXPOSE 1965

VOLUME /etc/certs
VOLUME /var/log/jgs
VOLUME /var/gemini

COPY docker-entrypoint.sh ./
COPY server.jar ./
COPY logback.xml ./
COPY lib  ./
COPY gemini-server.conf ./

ENTRYPOINT docker-entrypoint.sh
