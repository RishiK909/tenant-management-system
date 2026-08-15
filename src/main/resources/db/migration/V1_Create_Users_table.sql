CREATE TABLE users
(
    id                      UUID NOT NULL ,
    user_name               VARCHAR(255) NOT NULL,
    email                   VARCHAR(255) NOT NULL,
    password                VARCHAR(255) NOT NULL,
    phone_number            VARCHAR(20) NOT NULL,
    address                 VARCHAR(255) NOT NULL,
    role                    VARCHAR(255) NOT NULL,

    created_at              TIMESTAMP WITHOUT TIME ZONE,
    created_by              UUID,
    updated_by              UUID,
    updated_at              TIMESTAMP WITHOUT TIME ZONE,
    deleted_at              TIMESTAMP WITHOUT TIME ZONE,
    status                  VARCHAR(255),

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_phone_number UNIQUE (phone_number)
);