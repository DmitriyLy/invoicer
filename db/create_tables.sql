DROP TABLE IF EXISTS UserRoles;
DROP SEQUENCE IF EXISTS userroles_id_seq;

DROP TABLE IF EXISTS Users;
DROP SEQUENCE IF EXISTS users_id_seq;

DROP TABLE IF EXISTS Roles;
DROP SEQUENCE IF EXISTS roles_id_seq;

CREATE TABLE Users
(
    id           BIGSERIAL    NOT NULL,
    first_name   VARCHAR(50)  NOT NULL,
    last_name    VARCHAR(50)  NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    phone        VARCHAR(20)           DEFAULT NULL,
    password     VARCHAR(255)          DEFAULT NULL,
    address      VARCHAR(255)          DEFAULT NULL,
    title        VARCHAR(50)           DEFAULT NULL,
    bio          VARCHAR(255)          DEFAULT NULL,
    enabled      BOOLEAN               DEFAULT FALSE,
    non_locked   BOOLEAN               DEFAULT FALSE,
    using_mfa    BOOLEAN               DEFAULT FALSE,
    image_url    VARCHAR(255)          DEFAULT NULL,
    created_date TIMESTAMP    NOT NULL DEFAULT NOW(),

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
    id      BIGSERIAL NOT NULL UNIQUE,
    user_id BIGINT    NOT NULL,
    role_id BIGINT    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES Users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES Roles (id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);
