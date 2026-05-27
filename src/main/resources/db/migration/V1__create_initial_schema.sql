CREATE TABLE ALBUM
(
    id           uuid PRIMARY KEY,
    name         VARCHAR(8) NOT NULL UNIQUE,
    description  VARCHAR(80),
    analog       BOOLEAN NOT NULL DEFAULT FALSE,
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
    name         VARCHAR(20),
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

CREATE TABLE PHOTO_GANG_BANGER
(
    id                  uuid PRIMARY KEY,
    date_created        DATE NOT NULL DEFAULT CURRENT_DATE,
    relationship_status VARCHAR(15),
    semester_start      VARCHAR(20),
    first_name          VARCHAR(20),
    last_name           VARCHAR(20),
    username            VARCHAR(20) UNIQUE,
    email               VARCHAR(50),
    is_active           BOOLEAN DEFAULT TRUE,
    is_pang             BOOLEAN DEFAULT FALSE,
    profile_picture     VARCHAR(150),
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

/* Only one active position per member */
CREATE UNIQUE INDEX one_active_position_per_member
    ON PHOTO_GANG_BANGER_TO_POSITION (photo_gang_banger_id)
    WHERE semester_end IS NULL;

CREATE TABLE MOTIVE
(
    id              uuid PRIMARY KEY,
    title           VARCHAR(100),
    date            DATE NOT NULL DEFAULT CURRENT_DATE,
    date_created    DATE NOT NULL DEFAULT CURRENT_DATE,
    category_id     UUID REFERENCES CATEGORY (id),
    event_owner_id  UUID REFERENCES EVENT_OWNER (id),
    album_id        UUID REFERENCES ALBUM (id),
    analog_album_id UUID REFERENCES ALBUM (id),
    place_id        UUID REFERENCES PLACE (id),
    security_level  VARCHAR(50) CHECK (security_level IN ('FG', 'HUSFOLK', 'ALLE')),
    date_deleted    DATE DEFAULT NULL
);

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
    date_created         DATE NOT NULL DEFAULT CURRENT_DATE,
    image_number         INTEGER NOT NULL,
    page_number          INTEGER NOT NULL,
    security_level       VARCHAR(50) CHECK (security_level IN ('FG', 'HUSFOLK', 'ALLE')),
    image_prod           VARCHAR(255), 
    image_web            VARCHAR(255),
    image_thumb          VARCHAR(255) NOT NULL,
    good_picture         BOOLEAN,
    analog               BOOLEAN NOT NULL DEFAULT FALSE,
    motive_id            UUID REFERENCES MOTIVE (id),
    photo_gang_banger_id UUID REFERENCES PHOTO_GANG_BANGER (id),
    other_meta_data      JSONB,
    date_deleted         DATE DEFAULT NULL,
    gang_id              UUID REFERENCES GANG (id)
);

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