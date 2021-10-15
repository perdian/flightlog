FROM openjdk:17-slim

VOLUME /var/flightlog/database/

COPY target/flightlog.jar /var/flightlog/flightlog.jar

CMD ["java", "-jar", "/var/flightlog/flightlog.jar"]
