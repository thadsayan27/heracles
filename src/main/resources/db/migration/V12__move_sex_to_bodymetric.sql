ALTER TABLE body_metric
    ADD sex VARCHAR(255);

UPDATE body_metric bm
SET sex = u.sex
FROM users u
WHERE bm.user_id = u.id;

UPDATE body_metric
SET sex = 'OTHER'
WHERE sex IS NULL;

ALTER TABLE body_metric
    ALTER COLUMN sex SET NOT NULL;

ALTER TABLE users
    DROP COLUMN sex;