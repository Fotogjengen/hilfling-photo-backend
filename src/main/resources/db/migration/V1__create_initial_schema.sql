CREATE TABLE ALBUM
(
    id           uuid PRIMARY KEY,
    name         VARCHAR(8) NOT NULL UNIQUE,
    description  VARCHAR(80),
    analog       BOOLEAN NOT NULL DEFAULT FALSE,
    is_default   BOOLEAN NOT NULL DEFAULT FALSE,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE MEDIA
(
    id           uuid PRIMARY KEY,
    medium       VARCHAR(80) NOT NULL UNIQUE,
    analog       BOOLEAN NOT NULL,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE POSITION
(
    id           uuid PRIMARY KEY,
    title        VARCHAR(40),
    email        VARCHAR(40),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE POSITION_TO_PERMISSION
(
    position_id UUID NOT NULL REFERENCES POSITION (id) ON DELETE CASCADE,
    permission  VARCHAR(40) NOT NULL,
    PRIMARY KEY (position_id, permission)
);

CREATE TABLE GANG
(
    id           uuid PRIMARY KEY,
    name         VARCHAR(30),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE PURCHASE_ORDER
(
    id           uuid PRIMARY KEY,
    name         VARCHAR(30),
    email        VARCHAR(50),
    address      VARCHAR(40),
    zip_code     INTEGER,
    city         VARCHAR(30),
    send_by_post BOOLEAN,
    comment      TEXT,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    is_completed BOOLEAN,
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE PLACE
(
    id           uuid PRIMARY KEY NOT NULL,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    name         VARCHAR(30) UNIQUE,
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE EVENT_OWNER
(
    id           uuid PRIMARY KEY,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    name         VARCHAR(30),
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE CATEGORY
(
    id           uuid PRIMARY KEY,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    name         VARCHAR(80),
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE PHOTOGRAPHY_REQUEST
(
    id           uuid PRIMARY KEY,
    date_created DATE        NOT NULL DEFAULT CURRENT_DATE,
    start_time   DATE        NOT NULL,
    end_time     DATE        NOT NULL,
    place        VARCHAR(40) NOT NULL,
    is_intern    BOOLEAN     NOT NULL DEFAULT FALSE,
    type         VARCHAR(40) NOT NULL,
    name         VARCHAR(50) NOT NULL,
    email        VARCHAR(50) NOT NULL,
    phone        VARCHAR(20) NOT NULL,
    description  TEXT        NOT NULL,
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE EXTERNAL_USER
(
    id           uuid PRIMARY KEY,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    username     VARCHAR(20),
    password     VARCHAR(255),
    email        VARCHAR(50),
    full_name    VARCHAR(50),
    security_level VARCHAR(50) CHECK (security_level IN ('FG', 'HUSFOLK', 'ALLE')), 
    description  TEXT,
    is_active    BOOLEAN DEFAULT TRUE,
    date_deleted DATE DEFAULT NULL
);

/* Soft-deleted external users should not block reuse of their username */
CREATE UNIQUE INDEX uq_external_user_username_active ON EXTERNAL_USER (username)
    WHERE date_deleted IS NULL;

CREATE TABLE PHOTO_GANG_BANGER
(
    id                  uuid PRIMARY KEY,
    date_created        DATE NOT NULL DEFAULT CURRENT_DATE,
    food_preference     VARCHAR(40),
    birthday            DATE,
    semester_start      VARCHAR(20),
    name                VARCHAR(50),
    username            VARCHAR(20) UNIQUE,
    email               VARCHAR(50),
    is_active           BOOLEAN DEFAULT TRUE,
    is_pang             BOOLEAN DEFAULT FALSE,
    phone_number        VARCHAR(20),
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE PHOTO_GANG_BANGER_TO_POSITION
(
    photo_gang_banger_id UUID REFERENCES PHOTO_GANG_BANGER (id),
    position_id UUID REFERENCES POSITION (id),
    semester_start VARCHAR(20),
    semester_end VARCHAR(20) DEFAULT NULL,
    PRIMARY KEY (photo_gang_banger_id, semester_start),
    UNIQUE (position_id, semester_start)
);

CREATE TABLE MOTIVE
(
    id              uuid PRIMARY KEY,
    title           VARCHAR(900),
    date            DATE NOT NULL DEFAULT CURRENT_DATE,
    date_created    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    category_id     UUID REFERENCES CATEGORY (id),
    event_owner_id  UUID REFERENCES EVENT_OWNER (id),
    album_id        UUID REFERENCES ALBUM (id),
    analog_album_id UUID REFERENCES ALBUM (id),
    place_id        UUID REFERENCES PLACE (id),
    security_level  VARCHAR(50) CHECK (security_level IN ('FG', 'HUSFOLK', 'ALLE')),
    date_deleted    DATE DEFAULT NULL
);

/* Finding the photos stored in an album */
CREATE INDEX idx_motive_album_id ON MOTIVE (album_id);
CREATE INDEX idx_motive_analog_album_id ON MOTIVE (analog_album_id);

/* Ensures that a motive has the correct album types */
CREATE OR REPLACE FUNCTION check_motive_albums()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.album_id IS NOT NULL AND EXISTS (
        SELECT 1 FROM ALBUM WHERE id = NEW.album_id AND analog = TRUE
    ) THEN
        RAISE EXCEPTION 'album_id must reference a digital album';
    END IF;

    IF NEW.analog_album_id IS NOT NULL AND NOT EXISTS (
        SELECT 1 FROM ALBUM WHERE id = NEW.analog_album_id AND analog = TRUE
    ) THEN
        RAISE EXCEPTION 'analog_album_id must reference an analog album';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE PHOTO
(
    id                   uuid PRIMARY KEY,
    date_created         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    image_number         INTEGER NOT NULL,
    page_number          INTEGER NOT NULL,
    image_prod           VARCHAR(255), 
    image_web            VARCHAR(255),
    image_thumb          VARCHAR(255),
    good_picture         BOOLEAN,
    media_id             UUID NOT NULL REFERENCES MEDIA (id),
    motive_id            UUID REFERENCES MOTIVE (id),
    photo_gang_banger_id UUID REFERENCES PHOTO_GANG_BANGER (id),
    other_meta_data      JSONB,
    date_deleted         DATE DEFAULT NULL,
    gang_id              UUID REFERENCES GANG (id)
);

CREATE INDEX idx_photo_media_id ON PHOTO (media_id);

/* Foreign keys are not indexed automatically; every motive page and album check looks photos up by motive */
CREATE INDEX idx_photo_motive_id ON PHOTO (motive_id);

/* Good pictures feed: paged newest first, tie-broken by id */
CREATE INDEX idx_photo_good_feed ON PHOTO (date_created DESC, id)
    WHERE good_picture AND date_deleted IS NULL;

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

/* Reserved image/page number. After an upload finishes, this should be empty*/
CREATE TABLE PHOTO_RESERVATION
(
    album_id     UUID NOT NULL REFERENCES ALBUM (id),
    page_number  INTEGER NOT NULL,
    image_number INTEGER NOT NULL,
    reserved_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (album_id, page_number, image_number)
);


CREATE TRIGGER enforce_motive_albums
    BEFORE INSERT OR UPDATE ON MOTIVE
    FOR EACH ROW EXECUTE FUNCTION check_motive_albums();

CREATE TABLE PHOTOS_IN_PURCHASE_ORDER
(
    purchase_order_id UUID REFERENCES PURCHASE_ORDER (id),
    photo_id          UUID REFERENCES PHOTO (id),
    img_size          VARCHAR(10),
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE USER_UPLOADS
(
    id                   uuid PRIMARY KEY,
    date_created         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    security_level       VARCHAR(50) CHECK (security_level IN ('FG', 'ALLE')),
    link                 VARCHAR(255),
    photo_gang_banger_id UUID REFERENCES PHOTO_GANG_BANGER (id) NOT NULL,
    date_deleted         DATE DEFAULT NULL
);

/* FK needs to be added via ALTER to USER_UPLOADS since it references PHOTO_GANG_BANGER which leads to a circular dependency */
ALTER TABLE PHOTO_GANG_BANGER
    ADD COLUMN profile_picture UUID REFERENCES USER_UPLOADS (id);

/* Trigram indexes for cross-entity filter search autocomplete */
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_place_name_trgm ON place USING GIN (name gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_event_owner_name_trgm ON event_owner USING GIN (name gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_category_name_trgm ON category USING GIN (name gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_album_name_trgm ON album USING GIN (name gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_album_description_trgm ON album USING GIN (description gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_motive_title_trgm ON motive USING GIN (title gin_trgm_ops);
