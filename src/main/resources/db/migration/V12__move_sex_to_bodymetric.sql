ALTER TABLE body_metric
    ADD sex VARCHAR(255);

ALTER TABLE body_metric
    ALTER COLUMN sex SET NOT NULL;

ALTER TABLE users
    DROP COLUMN sex;