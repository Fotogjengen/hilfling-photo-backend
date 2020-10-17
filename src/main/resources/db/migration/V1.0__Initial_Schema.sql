CREATE TABLE ALBUM
(
    id           uuid PRIMARY KEY,
    title        VARCHAR(40),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    is_analog    BOOLEAN
);

CREATE TABLE POSITION
(
    id           uuid PRIMARY KEY,
    title        VARCHAR(40),
    email        VARCHAR(40),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE GANG
(
    id           uuid PRIMARY KEY,
    name         VARCHAR(30),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE PHOTO_TAG
(
    id           uuid PRIMARY KEY,
    name         VARCHAR(20),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE PURCHASE_ORDER
(
    id           uuid PRIMARY KEY,
    name         VARCHAR(30),
    email        VARCHAR(30),
    address      VARCHAR(40),
    zip_code     INTEGER,
    city         VARCHAR(30),
    send_by_post BOOLEAN,
    comment      TEXT,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    is_completed BOOLEAN
);

CREATE TABLE SECURITY_LEVEL
(
    id           uuid PRIMARY KEY,
    type         VARCHAR(10),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE PLACE
(
    id           uuid PRIMARY KEY,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    name         VARCHAR(30)
);

CREATE TABLE EVENT_OWNER
(
    id           uuid PRIMARY KEY,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    name         VARCHAR(30)
);

CREATE TABLE CATEGORY
(
    id           uuid PRIMARY KEY,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    name         VARCHAR(20)
);

CREATE TABLE ARTICLE_TAG
(
    id           uuid PRIMARY KEY,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    name         VARCHAR(20)
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
    description  TEXT        NOT NULL
);

CREATE TABLE SAMFUNDET_USER
(
    id                uuid PRIMARY KEY,
    date_created      DATE NOT NULL DEFAULT CURRENT_DATE,
    first_name        VARCHAR(20),
    last_name         VARCHAR(20),
    username          VARCHAR(20),
    email             VARCHAR(30),
    profile_picture   VARCHAR(100),
    phone_number      VARCHAR(20),
    sex               VARCHAR(10),
    security_level_id UUID REFERENCES SECURITY_LEVEL (id)
);

CREATE TABLE PHOTO_GANG_BANGER
(
    id                  uuid PRIMARY KEY,
    date_created        DATE NOT NULL DEFAULT CURRENT_DATE,
    relationship_status VARCHAR(15),
    semester_start      VARCHAR(20),
    is_active           BOOLEAN,
    is_pang             BOOLEAN,
    address             VARCHAR(40),
    zip_code            VARCHAR(4),
    city                VARCHAR(30),
    samfundet_user_id   UUID REFERENCES SAMFUNDET_USER (id)
);

CREATE TABLE ARTICLE
(
    id                   uuid PRIMARY KEY,
    date_created         DATE NOT NULL DEFAULT CURRENT_DATE,
    title                VARCHAR(50),
    plain_text           VARCHAR(100),
    security_level_id    UUID REFERENCES SECURITY_LEVEL (id),
    photo_gang_banger_id UUID REFERENCES PHOTO_GANG_BANGER (id)
);

CREATE TABLE MOTIVE
(
    id             uuid PRIMARY KEY,
    title          VARCHAR(20),
    date_created   DATE NOT NULL DEFAULT CURRENT_DATE,
    category_id    UUID REFERENCES CATEGORY (id),
    event_owner_id UUID REFERENCES EVENT_OWNER (id),
    album_id       UUID REFERENCES ALBUM (id)
);

CREATE TABLE PHOTO
(
    id                   uuid PRIMARY KEY,
    date_created         DATE NOT NULL DEFAULT CURRENT_DATE,
    small_url            VARCHAR(40),
    medium_url           VARCHAR(40),
    large_url            VARCHAR(40),
    is_good_picture      BOOLEAN,
    motive_id            UUID REFERENCES MOTIVE (id),
    place_id             UUID REFERENCES PLACE (id),
    security_level_id    UUID REFERENCES SECURITY_LEVEL (id),
    gang_id              UUID REFERENCES GANG (id),
    photo_gang_banger_id UUID REFERENCES PHOTO_GANG_BANGER (id)
);

CREATE TABLE ANALOG_PHOTO
(
    image_number INTEGER,
    date_created DATE NOT NULL DEFAULT CURRENT_DATE,
    page_number  INTEGER,
    photo_id     UUID REFERENCES PHOTO (id)
);


CREATE TABLE ARTICLE_TAG_IN_ARTICLE
(
    article_tag_id UUID REFERENCES ARTICLE_TAG (id),
    article_id     UUID REFERENCES ARTICLE (id)
);

CREATE TABLE PHOTOS_IN_PURCHASE_ORDER
(
    purchase_order_id UUID REFERENCES PURCHASE_ORDER (id),
    photo_id          UUID REFERENCES PHOTO (id),
    img_size          VARCHAR(10)
);

CREATE TABLE PHOTO_TAG_IN_PHOTO
(
    photo_tag_id UUID REFERENCES PHOTO_TAG (id),
    photo_id     UUID REFERENCES PHOTO (id)
);