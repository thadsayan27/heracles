
-- workout indexes
CREATE INDEX IF NOT EXISTS idx_workouts_user_id ON workouts (user_id);
CREATE INDEX IF NOT EXISTS idx_workout_exercises_workout_id ON workout_exercises (workout_id);
CREATE INDEX IF NOT EXISTS idx_workout_exercises_exercise_id ON workout_exercises (exercise_id);
CREATE INDEX IF NOT EXISTS idx_workout_sets_workout_exercise_id ON workout_sets (workout_exercise_id);

-- exercise indexes
CREATE INDEX IF NOT EXISTS idx_exercises_lower_name ON exercises (lower(name));
CREATE INDEX IF NOT EXISTS idx_exercises_primary_muscle_group ON exercises (primary_muscle_group);
CREATE INDEX IF NOT EXISTS idx_exercises_user_id ON exercises (user_id);

-- refresh token indexes
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_family_active ON refresh_tokens (family_id) WHERE revoked = false;
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_active ON refresh_tokens (user_id) WHERE revoked = false;


CREATE INDEX IF NOT EXISTS idx_workout_exercises_workout_id_exercisenum ON workout_exercises (workout_id, exercise_num);


