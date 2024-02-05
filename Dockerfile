FROM openjdk:21-slim

VOLUME /var/flightlog/database/

ENV FLIGHTLOG_DB_DIRECTORY=/var/flightlog/database
ENV FLIGHTLOG_AUTHENTICATION_TYPE=fixed

COPY target/flightlog.jar /var/flightlog/app/flightlog.jar

CMD ["java", "-jar", "/var/flightlog/app/flightlog.jar"]
