FROM openjdk:18-slim

VOLUME /var/flightlog/database/

COPY target/flightlog.war /var/flightlog/app/flightlog.war

CMD ["java", "-jar", "/var/flightlog/app/flightlog.war"]
