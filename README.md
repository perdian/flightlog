# Introduction

[![Releases](https://img.shields.io/github/v/release/perdian/flightlog)](https://github.com/perdian/flightlog/releases)
[![Build](https://img.shields.io/circleci/build/github/perdian/flightlog/master)](https://circleci.com/gh/perdian/flightlog)
[![License](http://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

Flightlog was born from my nerdy need for a bookkeeping tool to track all the flights I have taken so far.

For years, I was quite happy with flugstatistik.de, but unfortunately, it hasn't evolved much over the past few years, resulting in a UI that looks quite outdated and a feature set that is okay-ish, but lacking some elements I would like to see. Additionally, there was (and still is!) no way to back up the data stored there, which leads to considerable vendor lock-in. What happens if they decide to stop providing their service, or if I simply forget my login credentials? No, that's just not acceptable.

From what I observed at the time (and still see today), there are no real alternatives. Openflights.org is an excellent idea, but the UI is even worse than flugstatistik.de's, looking as though it was thrown together in an hour or two. Don't get me wrong, I'm pretty sure a significant amount of time has been invested, but to me, it just looks terrible.

Therefore, with no suitable tool available, I decided to create one myself. The overall UI somewhat incorporates ideas from flugstatistik.de but is a bit more modern and streamlined.

![Home screen](docs/screenshots/home.png)

# Build

Flightlog is a [Spring Boot application](https://spring.io/guides/gs/spring-boot/) and can be built and executed accordingly:

    $ git clone https://github.com/perdian/flightlog.git
    $ cd flightlog
    $ mvn clean package

This creates the application JAR file into `target/flightlog.jar`

# Run

## JAR

The JAR file already contains everything needed to start Flighlog with its default settings:

    $ mvn clean package
    $ java -jar target/flightlog.jar

The application will now start and is accessible at

    http://localhost:8080/flightlog/

An embedded H2 database file will be stored at `~/.flightlog/`. Both the database location as well as the database type can be configured using environment variables (see the **Configuration** section for details).

## Docker container

### Fetch from Docker Hub

An alternative do running the JAR file directly is to create a Docker container that wraps the complete application.

Releases are automatically pushed to Docker Hub so all you need to do is to run the Docker image.

    $ docker run -p 8080:8080 perdian/flightlog:latest

Replace `latest` version with the latest release which can be found at https://github.com/perdian/flightlog/releases.

The application will be available on the machine on which you're executing the container at:

    http://localhost:8080/flightlog/

An embedded H2 database file will stored as volume for the created container. It can be configured using environment variables (see the **Configuration** section for details).

To persist the database you have to mount the directory `/var/flightlog/database/` from the container to somewhere on your host machine:

    $ docker run -v /path/to/your/host/directory:/var/flightlog/database -p 8080:8080 perdian/flightlog

### Build from sources

If you want to build the Docker image yourself from the sources it can be done like this:

    $ mvn clean package
    $ docker build -t perdian/flightlog .

After the container is built you can verify the application by running it:

    $ docker run -p 8080:8080 perdian/flightlog

The application will be available on the machine on which you're executing the container at:

    http://localhost:8080/flightlog/

# Configuration

The complete configuration can be done through environment variables so whether you're launching the application directly from the JAR file or via a Docker container the same set of environment variables is used.

## Database

By default an embedded H2 database is used but any database supported by Hibernate can be configured. The JDBC drivers for PostgreSQL and MySQL are already bundled within the standard Spring Boot application and Docker image.

| Environment variable | Default value | Comment |
| -------------------- | ------------- | ------- |
| `FLIGHTLOG_DB_URL` | `jdbc:h2:${FLIGHTLOG_DB_DIRECTORY:~/.flightlog/}/flightlogdb` | The complete JDBC URL of the database |
| `FLIGHTLOG_DB_DRIVER_CLASS_NAME` | `org.h2.Driver` | The JDBC driver class name. The class must be accessible on the classpath. |
| `FLIGHTLOG_DB_USERNAME` | `sa` | The JDBC username |
| `FLIGHTLOG_DB_PASSWORD` | | The JDBC password |
| `FLIGHTLOG_DB_DIRECTORY`| `~/.flightlog/` for the JAR distribution, `/var/flightlog/database/` for the Docker distribution | The directory where the embedded H2 database is located. Will only be used if `FLIGHTLOG_DB_URL` is not overwritten. |

## Authentication

The application ships with a few options of authentication providers available.

By default no authentication is required, which means without any additional configuration the application works in a single user mode: All flights are implicitly linked to a single user named `example@example.com`.

## OAuth2 authentication

Flightlog supports OAuth2 authentication via the following providers:

* Google

To enable the OAuth authentication you have to set the `FLIGHTLOG_AUTHENTICATION_TYPE` environment variable to `oauth2`

For each login the application will perform an OAuth2 authentication. After the successful authentication an entry within the `flightuser_user` will be made, populated with the values retrieved from the OAuth2 provider.

The following environment variables have to be set to the OAuth2 credentials retrieved from the Google Cloud console:

* `FLIGHTLOG_AUTHENTICATION_OAUTH2_GOOGLE_CLIENT_ID`
* `FLIGHTLOG_AUTHENTICATION_OAUTH2_GOOGLE_CLIENT_SECRET`

## Whitelisting users

By default no new users can be registered in a flightlog applcation. This is a conscious choice to prohibit someone spamming the system and creating entries in the database by simply performing multiple OAuth2 authentications.

There are two ways to allow an authentication from a new OAuth2 account:

### Disabling registration blocker

By setting the environment variable `FLIGHTLOG_REGISTRATION_ALLOW_BY_DEFAULT` to `true` will bypass the check for existence and will create a new entry in the local database for every new account authenticated via OAuth2.

### Adding the email address to the whitelist

When a new OAuth2 account is found the system will check the internal *whitelist* whether the email address of the new user is found in the whitelist. If that's the case then the new user will be added into the database and the authentication will succeed.

To place one (or more) user(s) on the whitelist add the email addresses to the environment variable `FLIGHTLOG_REGISTRATION_EMAIL_ADDRESSES_ALLOWLIST` (separate multiple addresses by comma).

A second option (which is mainly designed for a personal installation without any open registration) is to add the whitelisted email addresses directly as environment variable `FLIGHTLOG_AUTHENTICATION_REGISTRATION_EMAIL_WHITELIST`:

## Other environment variables

| Environment variable | Default value | Comment |
| -------------------- | ------------- | ------- |
| `FLIGHTLOG_SERVER_PORT` | `8080` | The port on which the application will listen for incoming requests |
| `FLIGHTLOG_SERVER_CONTEXT_PATH` | `/flighlog` | The context path under which the application will be made available. The default value `/flightlog` implies that when using the default port of `8080` the applications main page can be reached at `http://localhost:8080/flightlog`. |
| `FLIGHTLOG_SERVER_COOKIE_NAME` | `flightlog-session` | The name of the cookie that is used to store session related data. |

# Credits

This application would not be possible without the great work of other open source projects.

A big thank you therefore goes to:

* Openflights.org for its collection of airport and airline data as well as inspiration of how to display the flights on the map (<https://openflights.org/data.html>).
* Openlayers for the ground work in providing the world map (<https://openlayers.org/>).
* arc.js for handling the pain of computing a great circle between two airports (<https://github.com/springmeyer/arc.js/>).
* Semantic UI for its beautiful frontend components (<https://semantic-ui.com/>).
* JQuery for the ultimate DOM manipulation tool (<https://jquery.com/>).
* Spring Boot for the heavy lifting in the backend (<https://projects.spring.io/spring-boot/>).
* Hibernate for the SQL abstraction layer (<http://hibernate.org/>).

# License

Flightlog is licensed under the Apache Licence 2.0 (<http://www.apache.org/licenses/>).
