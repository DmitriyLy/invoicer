DROP TABLE IF EXISTS UserRoles;
DROP SEQUENCE IF EXISTS userroles_id_seq;

DROP TABLE IF EXISTS Users;
DROP SEQUENCE IF EXISTS users_id_seq;

DROP TABLE IF EXISTS Roles;
DROP SEQUENCE IF EXISTS roles_id_seq;

CREATE TABLE Users
(
    id         BIGSERIAL NOT NULL,
    first_name VARCHAR(50),
    last_name  VARCHAR(50),
    email      VARCHAR(100),
    phone      VARCHAR(20),
    PRIMARY KEY (id)
);

CREATE TABLE Roles
(
    id          BIGSERIAL    NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    permissions VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE UserRoles
(
    id      BIGSERIAL NOT NULL,
    user_id BIGINT    NOT NULL,
    role_id BIGINT    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES Users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES Roles(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    UNIQUE (user_id, role_id)
);
