/* Soft-deleted external users should not block reuse of their username */
ALTER TABLE EXTERNAL_USER DROP CONSTRAINT external_user_username_key;
CREATE UNIQUE INDEX uq_external_user_username_active ON EXTERNAL_USER (username)
    WHERE date_deleted IS NULL;
