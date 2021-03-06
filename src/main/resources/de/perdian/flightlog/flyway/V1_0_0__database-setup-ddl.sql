DROP TABLE IF EXISTS `flight`;
CREATE TABLE `flight` (
  `id` BINARY(16) NOT NULL,
  `aircraft_name` VARCHAR(100),
  `aircraft_registration` VARCHAR(10),
  `aircraft_type` VARCHAR(50),
  `airline_code` CHAR(2),
  `airline_name` VARCHAR(100),
  `arrival_airport_code` CHAR(3),
  `arrival_date_local` DATE,
  `arrival_time_local` TIME,
  `cabin_class` VARCHAR(20),
  `comment` LONGTEXT,
  `departure_airport_code` CHAR(3),
  `departure_date_local` DATE,
  `departure_time_local` TIME,
  `flight_distance` INTEGER,
  `flight_duration` INTEGER,
  `flight_number` VARCHAR(10),
  `flight_reason` VARCHAR(20),
  `seat_number` VARCHAR(10),
  `seat_type` VARCHAR(20),
  `user_user_id` BINARY(16),
  PRIMARY KEY (`id`)
);

CREATE TABLE `user` (
  `user_id` BINARY(16) NOT NULL,
  `authentication_source` VARCHAR(64),
  `password` VARCHAR(64),
  `username` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`user_id`)
);

DROP TABLE IF EXISTS `registrationwhitelist`;
CREATE TABLE `registrationwhitelist` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(200),
  PRIMARY KEY (`id`)
);
