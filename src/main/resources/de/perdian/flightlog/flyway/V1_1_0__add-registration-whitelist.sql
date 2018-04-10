DROP TABLE IF EXISTS `registrationwhitelist`;
CREATE TABLE `registrationwhitelist` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(200),
  PRIMARY KEY (`id`)
);
