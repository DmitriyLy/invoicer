DROP TABLE IF EXISTS UserRoles;
DROP SEQUENCE IF EXISTS userroles_id_seq;

DROP TABLE IF EXISTS UserEvents;
DROP SEQUENCE IF EXISTS UserEvents_id_seq;

DROP TABLE IF EXISTS AccountVerifications;
DROP SEQUENCE IF EXISTS AccountVerifications_id_seq;

DROP TABLE IF EXISTS ResetPasswordVerifications;
DROP SEQUENCE IF EXISTS ResetPasswordVerifications_id_seq;

DROP TABLE IF EXISTS TwoFactorVerifications;
DROP SEQUENCE IF EXISTS TwoFactorVerifications_id_seq;

------------------------------------------

DROP TABLE IF EXISTS Users;
DROP SEQUENCE IF EXISTS users_id_seq;

DROP TABLE IF EXISTS Roles;
DROP SEQUENCE IF EXISTS roles_id_seq;

DROP TABLE IF EXISTS Events;
DROP SEQUENCE IF EXISTS events_id_seq;

CREATE TABLE Users
(
    id         BIGSERIAL    NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    phone      VARCHAR(20)           DEFAULT NULL,
    password   VARCHAR(255)          DEFAULT NULL,
    address    VARCHAR(255)          DEFAULT NULL,
    title      VARCHAR(50)           DEFAULT NULL,
    bio        VARCHAR(255)          DEFAULT NULL,
    enabled    BOOLEAN               DEFAULT FALSE,
    non_locked BOOLEAN               DEFAULT FALSE,
    using_mfa  BOOLEAN               DEFAULT FALSE,
    image_url  VARCHAR(255)          DEFAULT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),

    PRIMARY KEY (id)
);

CREATE TABLE Roles
(
    id         BIGSERIAL    NOT NULL,
    name       VARCHAR(50)  NOT NULL UNIQUE,
    permission VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE UserRoles
(
    id      BIGSERIAL NOT NULL,
    user_id BIGINT    NOT NULL UNIQUE,
    role_id BIGINT    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES Users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES Roles (id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE Events
(
    id          BIGSERIAL    NOT NULL,
    type        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE UserEvents
(
    id         BIGSERIAL NOT NULL,
    user_id    BIGINT    NOT NULL,
    event_id   BIGINT    NOT NULL,
    device     VARCHAR(100)       DEFAULT NULL,
    ip_address VARCHAR(100)       DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES Users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Events (id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE AccountVerifications
(
    id      BIGSERIAL    NOT NULL,
    user_id BIGINT       NOT NULL UNIQUE,
    url     VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES Users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE ResetPasswordVerifications
(
    id              BIGSERIAL    NOT NULL,
    user_id         BIGINT       NOT NULL UNIQUE,
    url             VARCHAR(255) NOT NULL UNIQUE,
    expiration_date TIMESTAMP    NOT NULL NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES Users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE TwoFactorVerifications
(
    id              BIGSERIAL   NOT NULL,
    user_id         BIGINT      NOT NULL UNIQUE,
    code            VARCHAR(10) NOT NULL UNIQUE,
    expiration_date TIMESTAMP   NOT NULL NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES Users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);