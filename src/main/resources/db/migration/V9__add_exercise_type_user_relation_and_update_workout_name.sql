ALTER TABLE exercises
    ADD exercise_type VARCHAR(255);

ALTER TABLE exercises
    ADD user_id UUID;

UPDATE exercises
SET exercise_type = 'PRESET'
WHERE user_id IS NULL;

ALTER TABLE exercises
    ALTER COLUMN exercise_type SET NOT NULL;

ALTER TABLE exercises
    ADD CONSTRAINT FK_EXERCISES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE workouts
    ALTER COLUMN name TYPE VARCHAR(255) USING (name::VARCHAR(255));

ALTER TABLE exercises
    ADD CONSTRAINT chk_exercise_user_required_unless_preset
        CHECK (
            user_id IS NOT NULL OR exercise_type = 'PRESET'
    );

ALTER TABLE refresh_tokens
    ALTER COLUMN revoked_at TYPE TIMESTAMP WITH TIME ZONE
        USING revoked_at AT TIME ZONE 'UTC';