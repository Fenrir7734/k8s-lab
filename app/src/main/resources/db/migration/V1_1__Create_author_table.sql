DROP TABLE IF EXISTS `shelf`;
DROP TABLE IF EXISTS `book`;
DROP TABLE IF EXISTS `genre`;
DROP TABLE IF EXISTS `author`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `author` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `firstname` varchar(50) NOT NULL,
    `lastname` varchar(50) NOT NULL,
    `birth_date` date NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `death_date` date DEFAULT NULL,
    `description` varchar(1000) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE `genre` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_name` (`name`)
    );

CREATE TABLE `book` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` date DEFAULT NULL,
    `isbn` varchar(13) NOT NULL,
    `pages` int NOT NULL,
    `published` date NOT NULL,
    `title` varchar(255) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `author_id` bigint NOT NULL,
    `genre_id` bigint NOT NULL,
    `description` varchar(5000) DEFAULT NULL,
    `cover` longblob,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_isbn` (`isbn`),
    KEY `FK_author_id` (`author_id`),
    KEY `FK_genre_id` (`genre_id`),
    CONSTRAINT `FK_author_id` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`),
    CONSTRAINT `FK_genre_id` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`)
    );

CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `firstname` varchar(50) NOT NULL,
    `lastname` varchar(50) NOT NULL,
    `email` varchar(255) NOT NULL,
    `phone` varchar(11) DEFAULT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `password` varchar(60) NOT NULL,
    `role` varchar(255) DEFAULT NULL,
    `username` varchar(255) NOT NULL,
    `banned` bit(1) NOT NULL,
    `verified` bit(1) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_username` (`username`)
    );

CREATE TABLE `shelf` (
    `book_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    `review` varchar(1000) DEFAULT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `rate` int NOT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`book_id`,`user_id`),
    KEY `FK_user_id` (`user_id`),
    CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_book_id` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
    );

