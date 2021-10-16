FROM openjdk:17-slim

VOLUME /var/flightlog/database/

COPY target/flightlog.jar /var/flightlog/app/flightlog.jar

CMD ["java", "-jar", "/var/flightlog/app/flightlog.jar"]
