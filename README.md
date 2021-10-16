# Introduction

[![Releases](https://img.shields.io/github/v/release/perdian/flightlog)](https://github.com/perdian/flightlog/releases)
[![Build](https://img.shields.io/circleci/build/github/perdian/flightlog/master)](https://circleci.com/gh/perdian/flightlog)
[![License](http://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

Flightlog was born out of my nerdy necessity to have a bookkeeping tool of all the flights that I have undertaken so far.

For years I was quite happy with flugstatistik.de but unfortunately it hasn't really evolved over the last years leaving a UI that looks quite outdated and a feature set that is okayish but lacks some of the things I would like to see.
In addition there was (and still is!) no way to backup the data stored there, which leads to a formidable vendor lock-in.
What happens when they decide to stop providing their service or I simply forget my login credentials?
No, that's just not good enough.

From what I saw at the time (and from what I see today) there are no real alternatives.
Openflights.org is an excellent idea but the UI is even worse than flugstatistik.de, looking like it has been hacked together in an hour or two.
Don't get me wrong I'm pretty sure a good amount of time has been invested but for me it simply looks awful.

So, as no appropriate tool was available I simply decided to write one myself.
The overall UI somewhat borrows some of the ideas from flugstatistik.de but is a bit more modern and streamlined.

![Home screen](docs/screenshots/home.png)

# Build

Flightlog is a [Spring Boot application](https://spring.io/guides/gs/spring-boot/) and can be built and executed accordingly:

    $ git clone https://github.com/perdian/flightlog.git
    $ cd flightlog
    $ mvn clean package

This creates the application WAR file into `target/flightlog.war`

# Run

## WAR

The WAR file already contains everything needed to start Flighlog with its default settings:

    $ mvn clean package
    $ java -jar target/flightlog.war

The application will now start and accessible at

    http://localhost:8080/flightlog/

An embedded H2 database file will stored at `/var/flightlog/database`. Both the database location as well as the database type can be configured using environment variables (see the **Configuration** section for details).

## Docker container

### Fetch from GitHub package registry

An alternative do running the WAR file directly is to create a Docker container that wraps the complete application.

Releases are automatically pushed to DockerHub packages so all you need to do is to run the Docker image.

    $ docker run -p 8080:8080 perdian/flightlog:1.0.0

Replace the `1.0.0` version with the latest release which can be found at https://github.com/perdian/flightlog/releases.

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

The complete configuration can be done using environment variables so whether you're launching the application directly from the WAR file or via a Docker container the same set of environment variables is used.

## Database

By default an embedded H2 database is used but any database supported by Hibernate can be configured. The JDBC drivers for PostgreSQL and MySQL are already bundled within the standard Spring Boot application and Docker image.

| Environment variable | Default value | Comment |
| -------------------- | ------------- | ------- |
| `FLIGHTLOG_DB_URL` | `jdbc:h2:/var/flightlog/database/flightlog` | The complete JDBC URL of the database |
| `FLIGHTLOG_DB_DRIVER_CLASS_NAME` | `org.h2.Driver` | The JDBC driver class name. The class must be accessible on the classpath. |
| `FLIGHTLOG_DB_USERNAME` | `sa` | The JDBC username |
| `FLIGHTLOG_DB_PASSWORD` | | The JDBC password |
| `FLIGHTLOG_DB_HIBERNATE_DIALECT` | `org.hibernate.dialect.H2Dialect` | The Hibernate dialect class (must correspond to the selected JDBC driver class) |

## Authentication

The application ships with a few options of authentication providers available.

By default no authentication is required, which means without any additional configuration the application works in a single user mode: All flights are implicitly linked to a single user.

If you want to enable authentication for the system you'll have to set the `FLIGHTLOG_AUTHENTICATION_REQUIRED` environment variable to `true` and configure additional environment variables that specify the authentication provider.

### Local database authentication

Using the internal database is the easiest option and is automatically selected if the `FLIGHTLOG_AUTHENTICATION_REQUIRED` environment variable has been set to `true`.

For each login the application will look for an entry within the `user` table that has `internaldatabase` as value of the `authentication_source`, a `username` column value equal to the value entered in the login form and a `password` column value equal to the SHA-256 hash (hex encoded) of the password entered in the login form.

If you want to disable the local authentication (because you want to only use other authentication methods) then you'll need to disable it by setting the `FLIGHTLOG_AUTHENTICATION_LOCAL_ENABLED` environment variable to `false`.

### LDAP authentication

Using an LDAP backend requires some additional environment variables to be set:

    FLIGHTLOG_AUTHENTICATION_LDAP_ENABLED="true"
    FLIGHTLOG_AUTHENTICATION_LDAP_URL="ldap://127.0.0.1"
    FLIGHTLOG_AUTHENTICATION_LDAP_BASE_DN="dc=example,dc=com"
    FLIGHTLOG_AUTHENTICATION_LDAP_USER_DN="ou=users"
    FLIGHTLOG_AUTHENTICATION_LDAP_BIND_DN="cn=x,dc=example,dc=com"
    FLIGHTLOG_AUTHENTICATION_LDAP_BIND_PASSWORD="yourbindpassword"
    FLIGHTLOG_AUTHENTICATION_LDAP_USERNAME_FIELD="uid"

The login will be made against the LDAP backend configured. After that a dummy user will be inserted into the local database with the `authentication_source` column set to `ldap` and the `username` column set to the username used during the login. The `password` column will remain `null`.

If both the local database authentication and the LDAP authentication have been activated the application will first check if a local user is found, and if that's the case will use the local user. Only if no local user is found will the application look for a user in the specified LDAP system.

## OAuth2 authentication

Flightlog supports OAuth2 authentication via the following providers:

* Google

To enable the OAuth authentication you have to set the `FLIGHTLOG_AUTHENTICATION_OAUTH2_ENABLED` environment variable to `true`

For each login the application will perform an OAuth2 authentication. After the successful authentication an entry within the `user` table that has `internaldatabase` will be made, populated with the values retrieved from the OAuth2 provider.

Note that in the default setup if the user authenticated via OAuth2 is *not* existing in the local database yet, then the authentication will fail. This is a conscious choice to prohibit someone spamming the system and creating entries in the database by simply performing multiple OAuth2 authentications.

There are two ways to allow an authentication from a new OAuth2 account:

### Disabling registration blocker

By setting the environment variable `FLIGHTLOG_AUTHENTICATION_REGISTRATION_RESTRICTED` to `false` will bypass the check for existence and will create a new entry in the local database for every new account authenticated via OAuth2.

### Adding the email address to the whitelist

When a new OAuth2 account is found the system will check the internal *whitelist* whether the email address of the new user is found in the whitelist. If that's the case then the new user will be added into the database and the authentication will succeed.

There are two places where the whitelisted email address are stored:

One option is to use the database table `registrationwhitelist` which simply contains an `email` column for the email to be whitelisted. To add members to the whitelist you can perform the following simple SQL statement against the local database:

    INSERT INTO registrationwhitelist (email) VALUES ('theaddress@example.com');

A second option (which is mainly designed for a personal installation without any open registration) is to add the whitelisted email addresses directly as environment variable `FLIGHTLOG_AUTHENTICATION_REGISTRATION_EMAIL_WHITELIST`:

### Detailed configuration: Google

To activate authentication via Google you'll have to provide your Google client id and client secret as additional environment variables:

    FLIGHTLOG_AUTHENTICATION_OAUTH2_GOOGLE_CLIENT_ID="12345"
    FLIGHTLOG_AUTHENTICATION_OAUTH2_GOOGLE_CLIENT_SECRET="67890"

For detailed information of how to create the credentials see https://developers.google.com/identity/protocols/OAuth2.

### All authentication related environment variables:

| Environment variable | Default value | Comment |
| -------------------- | ------------- | ------- |
| `FLIGHTLOG_AUTHENTICATION_REQUIRED` | `false` | Whether or not to require a user to be authenticated to access the application |
| `FLIGHTLOG_AUTHENTICATION_LOCAL_ENABLED` | `true` | Whether or not to include looking up the user from the internal database |
| `FLIGHTLOG_AUTHENTICATION_LDAP_ENABLED` | `false` | | Whether or not to include looking up the user from LDAP |
| `FLIGHTLOG_AUTHENTICATION_LDAP_BASE_DN` | | |
| `FLIGHTLOG_AUTHENTICATION_LDAP_URL` | | |
| `FLIGHTLOG_AUTHENTICATION_LDAP_USER_DN` | `ou=users` | |
| `FLIGHTLOG_AUTHENTICATION_LDAP_BIND_DN` | | |
| `FLIGHTLOG_AUTHENTICATION_LDAP_BIND_PASSWORD` | | |
| `FLIGHTLOG_AUTHENTICATION_LDAP_USERNAME_FIELD` | `uid` | |
| `FLIGHTLOG_AUTHENTICATION_OAUTH2_ENABLED` | `false`| Whether or not to allow OAuth2 for authenticating users |
| `FLIGHTLOG_AUTHENTICATION_OAUTH2_GOOGLE_CLIENT_ID` | | |
| `FLIGHTLOG_AUTHENTICATION_OAUTH2_GOOGLE_CLIENT_SECRET` | | |
| `FLIGHTLOG_AUTHENTICATION_REGISTRATION_RESTRICTED` | `true` | Whether or not to restrict the creation of new accounts |
| `FLIGHTLOG_AUTHENTICATION_REGISTRATION_EMAIL_WHITELIST` | | Comma separated list of email addresses which are allowed to create new accounts. Only valid if `FLIGHTLOG_AUTHENTICATION_REGISTRATION_RESTRICTED` is set to `true` |

## Automatic backup

Flightlog can automatically create a backup archive of the complete data (users including their logged flights). To enable the backup two environment variables need to be configured:

| Environment variable | Comment | Example |
| -------------------- | ------- | ------- |
| `FLIGHTLOG_BACKUP_CRON` | The CRON string specifying when the backup should take place | `0 0 5 * * 1` |
| `FLIGHTLOG_BACKUP_TARGET` | The target to which the backup should be written | `file:/var/flightlog/backup/` |

The backup archives themselves adhere to the naming convention `flightlog-backup-<date>.xml` where `<date>` will be replaced with the date on which the backup is being performed.

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
