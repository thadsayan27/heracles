ALTER TABLE workouts
    ADD COLUMN performed_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE workouts
    ADD COLUMN duration_minutes INTEGER;

ALTER TABLE workouts
    ADD CONSTRAINT ck_workouts_duration_minutes_positive
        CHECK (duration_minutes IS NULL OR duration_minutes >= 0);