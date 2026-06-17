ALTER TABLE refresh_tokens
    ALTER COLUMN family_id TYPE UUID USING (family_id::UUID);

ALTER TABLE refresh_tokens
    ALTER COLUMN jti TYPE UUID USING (jti::UUID);

ALTER TABLE refresh_tokens
    ALTER COLUMN replaced_by_jti TYPE UUID USING (replaced_by_jti::UUID);