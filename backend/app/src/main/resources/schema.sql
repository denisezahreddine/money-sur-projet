--  Désactiver temporairement les vérifications de clés étrangères pour éviter les erreurs
SET FOREIGN_KEY_CHECKS = 0;

--  Supprimer les tables si elles existent (dans l'ordre inverse des dépendances)
DROP TABLE IF EXISTS `supervision`;
DROP TABLE IF EXISTS `transaction`;
DROP TABLE IF EXISTS `contact`;
DROP TABLE IF EXISTS `payment_card`;
DROP TABLE IF EXISTS `invoice`;
DROP TABLE IF EXISTS `caregiver`;
DROP TABLE IF EXISTS `user`;

-- Réactiver les vérifications
SET FOREIGN_KEY_CHECKS = 1;

-- Table USER
CREATE TABLE IF NOT EXISTS `user` (
`id_user` BIGINT NOT NULL AUTO_INCREMENT,
`first_name` VARCHAR(100) NOT NULL,
`last_name` VARCHAR(100) NOT NULL,
`email` VARCHAR(180) NOT NULL UNIQUE,
`password_hash` VARCHAR(255) NOT NULL,
`pin_hash` VARCHAR(255) NULL,
`photo` TEXT NULL,
`type_profil` VARCHAR(20) NOT NULL, -- 'SENIOR' or 'FAMILY'
`balance` DECIMAL(19, 4) DEFAULT 0.0000,
`daily_limit` DECIMAL(19, 4) NULL,
`daily_spend` DECIMAL(19, 4) DEFAULT 0.0000,
 `pin_attempts` INT DEFAULT 0,
`is_email_verified` BOOLEAN DEFAULT FALSE,
`pin_locked_until` TIMESTAMP NULL,
`reset_token` VARCHAR(100) NULL,
`reset_expires_at` TIMESTAMP NULL,
 PRIMARY KEY (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- Table CAREGIVER (aidant)
CREATE TABLE IF NOT EXISTS `caregiver` (
`id_caregiver` BIGINT NOT NULL AUTO_INCREMENT,
`display_name` VARCHAR(100) NOT NULL,
`email` VARCHAR(180) NOT NULL UNIQUE,
`status` VARCHAR(20) NOT NULL,
`invitation_token` VARCHAR(100) NULL,
`token_expires_at` TIMESTAMP NULL,
`temp_data` JSON NULL,
PRIMARY KEY (`id_caregiver`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--  Table CONTACT (Carnet d'adresses interne)(bénéficiaire)
CREATE TABLE IF NOT EXISTS `contact` (
`id_contact` BIGINT NOT NULL AUTO_INCREMENT,
`nickname` VARCHAR(100) NULL,
`status` VARCHAR(20) NOT NULL,
`id_owner_user` BIGINT NOT NULL,
`id_target_user` BIGINT NOT NULL,
CONSTRAINT `fk_contact_owner` FOREIGN KEY (`id_owner_user`) REFERENCES `user` (`id_user`),
CONSTRAINT `unique_contact` UNIQUE (`id_owner_user`, `id_target_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--  Table INVOICE (Factures externes)
CREATE TABLE IF NOT EXISTS `invoice` (
`id_invoice` BIGINT NOT NULL AUTO_INCREMENT,
`issuer_name` VARCHAR(150) NOT NULL,
`issuer_iban` VARCHAR(34) NOT NULL,
`amount_due` DECIMAL(19, 4) NOT NULL,
`due_date` DATE NULL,
`proof_file_url` TEXT NOT NULL,
`reference` VARCHAR(100) NULL,
`statut` VARCHAR(20) NOT NULL,
`id_user` BIGINT NOT NULL,
PRIMARY KEY (`id_invoice`),
CONSTRAINT `fk_invoice_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--  Table PAYMENT_CARD
CREATE TABLE IF NOT EXISTS `payment_card` (`id_card` BIGINT NOT NULL AUTO_INCREMENT,
`alias` VARCHAR(100) NOT NULL,
`last_four` CHAR(4) NOT NULL,
`brand` VARCHAR(20) NULL,
`provider_token` VARCHAR(255) NOT NULL,
`expiry_date` DATE NOT NULL,
`id_user` BIGINT NOT NULL,
PRIMARY KEY (`id_card`),
CONSTRAINT `fk_card_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table TRANSACTION
CREATE TABLE IF NOT EXISTS `transaction` (
`id_transaction` BIGINT NOT NULL AUTO_INCREMENT,
`amount` DECIMAL(19, 4) NOT NULL,
`transaction_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 `validation_date` TIMESTAMP NULL,
`status` VARCHAR(20) NOT NULL,
`transaction_type` VARCHAR(30) NOT NULL,
`id_emitter_user` BIGINT NOT NULL,
`id_receiver_user` BIGINT NULL,
`id_invoice` BIGINT NULL,
`id_source_card` BIGINT NULL,
`id_validator_caregiver` BIGINT NULL,
PRIMARY KEY (`id_transaction`),
CONSTRAINT `fk_trans_emitter` FOREIGN KEY (`id_emitter_user`) REFERENCES `user` (`id_user`),
CONSTRAINT `fk_trans_receiver` FOREIGN KEY (`id_receiver_user`) REFERENCES `user` (`id_user`),
CONSTRAINT `fk_trans_invoice` FOREIGN KEY (`id_invoice`) REFERENCES `invoice` (`id_invoice`),
CONSTRAINT `fk_trans_card` FOREIGN KEY (`id_source_card`) REFERENCES `payment_card` (`id_card`), CONSTRAINT `fk_trans_caregiver` FOREIGN KEY (`id_validator_caregiver`) REFERENCES `caregiver` (`id_caregiver`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table de jointure SUPERVISION
CREATE TABLE IF NOT EXISTS `supervision` (
`id_user` BIGINT NOT NULL,
`id_caregiver` BIGINT NOT NULL,
PRIMARY KEY (`id_user`, `id_caregiver`),
CONSTRAINT `fk_supervision_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
CONSTRAINT `fk_supervision_caregiver` FOREIGN KEY (`id_caregiver`) REFERENCES `caregiver` (`id_caregiver`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;