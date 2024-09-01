ALTER TABLE snacks_and_boissons MODIFY COLUMN quantity BIGINT NULL;
ALTER TABLE snacks_and_boissons_history MODIFY COLUMN quantity BIGINT NULL;
ALTER TABLE snacks_and_boissons MODIFY COLUMN required_quantity INTEGER NULL;
CREATE TABLE IF NOT EXISTS `persistent_logins` (
    `username` VARCHAR(64) NOT NULL,
    `series` VARCHAR(64) NOT NULL,
    `token` VARCHAR(64) NOT NULL,
    `last_used` TIMESTAMP NOT NULL,
    PRIMARY KEY (`series`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;




