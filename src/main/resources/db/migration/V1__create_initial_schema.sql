CREATE TABLE ALBUM
(
    id           uuid PRIMARY KEY,
    title        VARCHAR(40),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    is_analog    BOOLEAN,
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE POSITION
(
    id           uuid PRIMARY KEY,
    title        VARCHAR(40),
    email        VARCHAR(40),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    date_deleted      DATE DEFAULT NULL
);

CREATE TABLE GANG
(
    id           uuid PRIMARY KEY,
    name         VARCHAR(30),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE PHOTO_TAG
(
    id           uuid PRIMARY KEY,
    name         VARCHAR(20),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    date_deleted      DATE DEFAULT NULL
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

CREATE TABLE ARTICLE_TAG
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
    date_deleted DATE DEFAULT NULL,
    semester_start VARCHAR(20),
    PRIMARY KEY (photo_gang_banger_id, semester_start),
    UNIQUE (position_id, semester_start)
);


CREATE TABLE ARTICLE
(
    id                   uuid PRIMARY KEY,
    date_created         DATE NOT NULL DEFAULT CURRENT_DATE,
    title                VARCHAR(50),
    plain_text           VARCHAR(1000),
    security_level       VARCHAR(50) CHECK (security_level IN ('FG', 'HUSFOLK', 'ALLE')),
    photo_gang_banger_id UUID REFERENCES PHOTO_GANG_BANGER (id),
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE MOTIVE
(
    id             uuid PRIMARY KEY,
    title          VARCHAR(100),
    date_created   DATE NOT NULL DEFAULT CURRENT_DATE,
    category_id    UUID REFERENCES CATEGORY (id),
    event_owner_id UUID REFERENCES EVENT_OWNER (id),
    album_id       UUID REFERENCES ALBUM (id),
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE PHOTO
(
    id                   uuid PRIMARY KEY,
    date_created         DATE NOT NULL DEFAULT CURRENT_DATE,
    small_url            VARCHAR(255), 
    medium_url           VARCHAR(255),
    large_url            VARCHAR(255) NOT NULL,
    is_good_picture      BOOLEAN,
    motive_id            UUID REFERENCES MOTIVE (id),
    place_id             UUID REFERENCES PLACE (id),
    security_level       VARCHAR(50) CHECK (security_level IN ('FG', 'HUSFOLK', 'ALLE')),
    gang_id              UUID REFERENCES GANG (id),
    album_id UUID REFERENCES ALBUM(id),
    category_id UUID REFERENCES CATEGORY(id),
    photo_gang_banger_id UUID REFERENCES PHOTO_GANG_BANGER (id),
    other_meta_data      JSONB,
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE ANALOG_PHOTO
(
    image_number INTEGER,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    page_number  INTEGER,
    photo_id     UUID REFERENCES PHOTO (id),
    date_deleted DATE DEFAULT NULL
);


CREATE TABLE ARTICLE_TAG_IN_ARTICLE
(
    article_tag_id UUID REFERENCES ARTICLE_TAG (id),
    article_id     UUID REFERENCES ARTICLE (id),
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE PHOTOS_IN_PURCHASE_ORDER
(
    purchase_order_id UUID REFERENCES PURCHASE_ORDER (id),
    photo_id          UUID REFERENCES PHOTO (id),
    img_size          VARCHAR(10),
    date_deleted DATE DEFAULT NULL
);

CREATE TABLE PHOTO_TAG_IN_PHOTO
(
    id           UUID PRIMARY KEY,
    photo_tag_id UUID REFERENCES PHOTO_TAG (id),
    photo_id     UUID REFERENCES PHOTO (id),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    date_deleted DATE DEFAULT NULL,
    unique (photo_tag_id, photo_id)
);