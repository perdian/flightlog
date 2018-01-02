DROP TABLE IF EXISTS `flight`;
CREATE TABLE `flight` (
  `id` INTEGER NOT NULL AUTO_INCREMENT,
  `aircraft_name` VARCHAR(100),
  `aircraft_registration` VARCHAR(10),
  `aircraft_type` VARCHAR(50),
  `airline_code` CHAR(2),
  `airline_name` VARCHAR(100),
  `arrival_airport_code` CHAR(3),
  `arrival_date_local` DATE,
  `arrival_time_local` TIME,
  `cabin_class` INTEGER,
  `comment` LONGTEXT,
  `departure_airport_code` CHAR(3),
  `departure_date_local` DATE,
  `departure_time_local` TIME,
  `flight_distance` INTEGER,
  `flight_duration` INTEGER,
  `flight_number` VARCHAR(10),
  `flight_reason` INTEGER,
  `seat_number` VARCHAR(10),
  `seat_type` INTEGER,
  `user_user_id` INTEGER,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `airline`;
CREATE TABLE `airline` (
  `code` CHAR(2) NOT NULL,
  `country_code` CHAR(2),
  `name` VARCHAR(100),
  `user_user_id` INTEGER,
  PRIMARY KEY (`code`)
);

CREATE TABLE `user` (
  `user_id` INTEGER NOT NULL AUTO_INCREMENT,
  `authentication_source` VARCHAR(64),
  `password` VARCHAR(64),
  `username` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`user_id`)
);
