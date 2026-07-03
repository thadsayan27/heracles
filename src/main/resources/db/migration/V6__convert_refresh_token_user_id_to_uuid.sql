DELETE FROM refresh_tokens
WHERE user_id IS NULL
   OR btrim(user_id) = ''
   OR btrim(user_id) !~* '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$';

ALTER TABLE refresh_tokens
    ALTER COLUMN user_id TYPE UUID USING btrim(user_id)::uuid,
    ALTER COLUMN user_id SET NOT NULL;