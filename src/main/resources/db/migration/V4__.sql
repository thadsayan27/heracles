CREATE TABLE refresh_tokens
(
    jti             VARCHAR(255)                NOT NULL,
    user_id         VARCHAR(255)                NOT NULL,
    family_id       VARCHAR(255)                NOT NULL,
    expires_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revoked         BOOLEAN                     NOT NULL,
    replaced_by_jti VARCHAR(255),
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (jti)
);