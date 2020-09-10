DROP DATABASE IF EXISTS pmb_database_test;

CREATE DATABASE pmb_database_test CHARACTER SET utf8mb4;

USE pmb_database_test;

CREATE TABLE user (
                id BIGINT AUTO_INCREMENT NOT NULL,
                lastname VARCHAR(50) NOT NULL,
                firstname VARCHAR(50) NOT NULL,
                email VARCHAR(80) NOT NULL,
				password BINARY(60) DEFAULT NULL,
                phone VARCHAR(16) NOT NULL,
                PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE app_account (
                id BIGINT AUTO_INCREMENT NOT NULL,
                user_id BIGINT NOT NULL,
                balance DECIMAL(8,2) DEFAULT NULL,
                PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE personal_payment (
                id BIGINT AUTO_INCREMENT NOT NULL,
                app_account_id BIGINT NOT NULL,
                amount DECIMAL(7,2) NOT NULL,
                cb_number CHAR(16) NOT NULL,
                cb_expiration_date_month CHAR(2) NOT NULL,
                cb_expiration_date_year CHAR(2) NOT NULL,
                cb_security_key CHAR(3) NOT NULL,
                PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE personal_transfer (
                id BIGINT AUTO_INCREMENT NOT NULL,
                app_account_id BIGINT NOT NULL,
                amount DECIMAL(7,2) NOT NULL,
                iban VARCHAR(31) NOT NULL,
                bic VARCHAR(11) NOT NULL,
                PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE transaction (
                id BIGINT AUTO_INCREMENT NOT NULL,
                app_account_sender_id BIGINT NOT NULL,
                app_account_beneficiary_id BIGINT NOT NULL,
                amount DECIMAL(8,2) NOT NULL,
                description VARCHAR(80) NOT NULL,
                transaction_date DATE NOT NULL,
                PRIMARY KEY (id, app_account_sender_id, app_account_beneficiary_id)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4;


CREATE INDEX transaction_idx
 ON transaction
 ( transaction_date );

CREATE TABLE relation (
                id BIGINT AUTO_INCREMENT NOT NULL,
                user_to_connect_id BIGINT NOT NULL,
                user_id BIGINT NOT NULL,
                PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE relation ADD CONSTRAINT user_connection_fk
FOREIGN KEY (user_id)
REFERENCES user (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE relation ADD CONSTRAINT user_connection_fk1
FOREIGN KEY (user_to_connect_id)
REFERENCES user (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE app_account ADD CONSTRAINT user_app_account_fk1
FOREIGN KEY (user_id)
REFERENCES user (id)
ON DELETE CASCADE
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT app_account_transaction_fk
FOREIGN KEY (app_account_sender_id)
REFERENCES app_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT app_account_transaction_fk1
FOREIGN KEY (app_account_beneficiary_id)
REFERENCES app_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE personal_transfer ADD CONSTRAINT app_account_personal_transfer_fk
FOREIGN KEY (app_account_id)
REFERENCES app_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE personal_payment ADD CONSTRAINT app_account_personal_payment_fk
FOREIGN KEY (app_account_id)
REFERENCES app_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
