FROM openliberty/open-liberty:20.0.0.6-full-java8-openj9-ubi-amd64

COPY --chown=1001:0  target/myapp-1.0.war /config/dropins/
COPY --chown=1001:0  server.xml /config/
