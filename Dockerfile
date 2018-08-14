FROM openjdk:10-jre-slim

VOLUME /var/flightlog/database/

COPY target/flightlog.war app.war

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-XX:+UnlockExperimentalVMOptions","-jar","/app.war"]
