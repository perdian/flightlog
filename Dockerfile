FROM openjdk:21-slim

COPY target/flightlog.jar /var/flightlog/app/flightlog.jar

CMD ["java", "-jar", "/var/flightlog/app/flightlog.jar"]
