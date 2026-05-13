CREATE TABLE body_metric
(
    user_id UUID    NOT NULL,
    weight  INTEGER NOT NULL,
    height  INTEGER NOT NULL,
    CONSTRAINT pk_body_metric PRIMARY KEY (user_id)
);

CREATE TABLE exercises
(
    id                   UUID         NOT NULL,
    name                 VARCHAR(255) NOT NULL,
    primary_muscle_group VARCHAR(255) NOT NULL,
    CONSTRAINT pk_exercises PRIMARY KEY (id)
);

CREATE TABLE routines
(
    id UUID NOT NULL,
    CONSTRAINT pk_routines PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            UUID         NOT NULL,
    email         VARCHAR(255) NOT NULL,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    sex           VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE workout_exercises
(
    id           UUID    NOT NULL,
    workout_id   UUID    NOT NULL,
    exercise_id  UUID    NOT NULL,
    exercise_num INTEGER NOT NULL,
    CONSTRAINT pk_workout_exercises PRIMARY KEY (id)
);

CREATE TABLE workout_sets
(
    id                  UUID         NOT NULL,
    workout_exercise_id UUID         NOT NULL,
    type                VARCHAR(255) NOT NULL,
    set_num             INTEGER      NOT NULL,
    weight              INTEGER      NOT NULL,
    repetitions         INTEGER      NOT NULL,
    CONSTRAINT pk_workout_sets PRIMARY KEY (id)
);

CREATE TABLE workouts
(
    id          UUID    NOT NULL,
    user_id     UUID    NOT NULL,
    workout_num INTEGER NOT NULL,
    CONSTRAINT pk_workouts PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE workouts
    ADD CONSTRAINT uc_workouts_user UNIQUE (user_id);

ALTER TABLE body_metric
    ADD CONSTRAINT FK_BODY_METRIC_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE workouts
    ADD CONSTRAINT FK_WORKOUTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE workout_exercises
    ADD CONSTRAINT FK_WORKOUT_EXERCISES_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES exercises (id);

ALTER TABLE workout_exercises
    ADD CONSTRAINT FK_WORKOUT_EXERCISES_ON_WORKOUT FOREIGN KEY (workout_id) REFERENCES workouts (id);

ALTER TABLE workout_sets
    ADD CONSTRAINT FK_WORKOUT_SETS_ON_WORKOUT_EXERCISE FOREIGN KEY (workout_exercise_id) REFERENCES workout_exercises (id);