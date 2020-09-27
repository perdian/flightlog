FROM openjdk:15-slim

VOLUME /var/flightlog/database/

COPY target/flightlog.war /var/flightlog/flightlog.war

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-XX:+UnlockExperimentalVMOptions", "-jar", "/var/flightlog/flightlog.war"]
