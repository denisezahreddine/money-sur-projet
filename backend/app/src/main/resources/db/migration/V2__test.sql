CREATE TABLE IF NOT EXISTS `flytest` (
                                         `id_user` BIGINT NOT NULL AUTO_INCREMENT,
                                         `first_name` VARCHAR(100) NOT NULL,
                                         PRIMARY KEY (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;