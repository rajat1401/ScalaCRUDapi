FROM openjdk:12-jre-alpine
WORKDIR /opt/docker
ADD opt /opt
RUN ["chown", "-R", "daemon:daemon", "."]
USER daemon
ENTRYPOINT ["/opt/docker/bin/todoapi"]
CMD []
