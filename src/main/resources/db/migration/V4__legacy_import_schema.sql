/* Old-site data (legacy-migration/): titles up to 900 chars, category names up to 24, unscanned photos without files */
ALTER TABLE MOTIVE
    ALTER COLUMN title TYPE VARCHAR(900);

ALTER TABLE CATEGORY
    ALTER COLUMN name TYPE VARCHAR(80);

ALTER TABLE PHOTO
    ALTER COLUMN image_thumb DROP NOT NULL;
