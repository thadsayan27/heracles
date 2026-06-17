ALTER TABLE refresh_tokens
    ADD revoked_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE refresh_tokens
    ALTER COLUMN created_at DROP NOT NULL;