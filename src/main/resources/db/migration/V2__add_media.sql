CREATE TABLE MEDIA
(
    id           uuid PRIMARY KEY,
    medium       VARCHAR(80) NOT NULL UNIQUE,
    analog       BOOLEAN NOT NULL,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    date_deleted DATE DEFAULT NULL
);

/* TODO: Remove this seed and the PHOTO backfill below when squashing migrations into V1 before launch */
INSERT INTO MEDIA (id, medium, analog)
VALUES (gen_random_uuid(), 'Digital', FALSE),
       (gen_random_uuid(), 'Analog farge', TRUE);

ALTER TABLE PHOTO
    ADD COLUMN media_id UUID REFERENCES MEDIA (id);

UPDATE PHOTO
SET media_id = (SELECT id FROM MEDIA WHERE medium = CASE WHEN PHOTO.analog THEN 'Analog farge' ELSE 'Digital' END);

ALTER TABLE PHOTO
    ALTER COLUMN media_id SET NOT NULL;

CREATE INDEX idx_photo_media_id ON PHOTO (media_id);

ALTER TABLE PHOTO
    DROP COLUMN analog;

/* A photo's security level is always its motive's; abort rather than silently loosen access */
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM PHOTO p LEFT JOIN MOTIVE m ON m.id = p.motive_id
        WHERE p.security_level IS DISTINCT FROM m.security_level
    ) THEN
        RAISE EXCEPTION 'PHOTO.security_level differs from its motive for some photos; resolve before migrating';
    END IF;
END;
$$;

ALTER TABLE PHOTO
    DROP COLUMN security_level;

/* Flipping analog would move photos between albums */
CREATE OR REPLACE FUNCTION check_media_analog_immutable()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.analog IS DISTINCT FROM OLD.analog AND EXISTS (
        SELECT 1 FROM PHOTO WHERE media_id = OLD.id
    ) THEN
        RAISE EXCEPTION 'Cannot change analog on a media that is referenced by photos';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER enforce_media_analog_immutable
    BEFORE UPDATE ON MEDIA
    FOR EACH ROW EXECUTE FUNCTION check_media_analog_immutable();
