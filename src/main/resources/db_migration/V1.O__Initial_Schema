CREATE TABLE "ALBUM" (
    "id" VARCHAR(6) PRIMARY KEY,
    "tittle" VARCHAR(40),
    "date_created" DATE,
    "is_analog" BIT
);

CREATE TABLE "CATEGORY" (
    "id" VARCHAR(6) PRIMARY KEY,
    "tittle" VARCHAR(20)
);

CREATE TABLE "ARTICLE_TAG" (
    "id" VARCHAR(6) PRIMARY KEY,
    "tag" VARCHAR(20)
)

CREATE TABLE "ARTICLE" (
    "id" VARCHAR(6) PRIMARY KEY,
    "tittle" VARCHAR(50),
    --TODO Figure out how to save content on page
    --TODO                  |
    --TODO                  V
    "plain_body_text" VARCHAR(100),
    "date_published" DATE
);

CREATE TABLE "MOTIVE" (
    "id" VARCHAR(6) PRIMARY KEY,
    "tittle" VARCHAR(20),
    "date" DATE,
    "category_id" VARCHAR(6) FOREIGN KEY,
    "event_owner_id" VARCHAR(6) FOREIGN KEY,
    "album_id" VARCHAR(6) FOREIGN KEY
);

CREATE TABLE "PHOTO_GANG_BANGER" (
    "id" VARCHAR(6) PRIMARY KEY,
    "relation_ship_status" VARCHAR(15),
    "semester_start" VARCHAR(20),
    "active" BIT,
    "pang" BIT,
    "Address" VARCHAR(40),
    "zip_code" INTEGER(4),
    "city" VARCHAR(30)
);

CREATE TABLE "ANALOG_PHOTO" (
    "image_number" INTEGER(10),
    "page_number" INTEGER(10)
);

CREATE TABLE "PHOTO" (
    "id" VARCHAR(6) PRIMARY KEY,
    "s_url" VARCHAR(40),
    "m_url" VARCHAR(40),
    "l_url" VARCHAR(40),
    "is_good_picture" BIT,
    "motive_id" VARCHAR(6) FOREIGN KEY,
    "place_id" VARCHAR(6) FOREIGN KEY,
    "security_id" VARCHAR(6) FOREIGN KEY
    "photo_gang_banger"
)

CREATE TABLE "PHOTO_TAG" (
    "id" VARCHAR(6) PRIMARY KEY,
    "tittle" VARCHAR(20)
);

CREATE TABLE "GANG" (
    "id" VARCHAR(6) PRIMARY KEY,
    "name" VARCHAR(30)
);

CREATE TABLE "SECURITY" (
    "id" VARCHAR(6) PRIMARY KEY,
    "type" VARCHAR(10),
);

CREATE TABLE "PLACE" (
    "id" VARCHAR(6) PRIMARY KEY,
    "location" VARCHAR(30)
);

CREATE TABLE "Event_owner" (
    "id" VARCHAR(6) PRIMARY KEY,
    "name" VARCHAR(30)
);

CREATE TABLE "USER"(
    "id" VARCHAR(6) PRIMARY KEY,
    "first_name" VARCHAR(20),
    "last_name" VARCHAR(20),
    "username" VARCHAR(20),
    "email" VARCHAR(30),
    --Link to img path
    "profile_picture" VARCHAR(100),
    "phone_number" INTEGER(8),
    "sex" CHAR(10),
    "security_id" VARCHAR(6) FOREIGN KEY

);

CREATE TABLE "POSITION"(
    "id" VARCHAR(6),
    "tittle" VARCHAR(40)
);

CREATE TABLE "PURCHASE_ORDER"(
    "id" VARCHAR(6) PRIMARY KEY,
    "name" CHAR(30),
    "email" VARCHAR(30),
    "Address" VARCHAR(40),
    "zip_code" INTEGER(4),
    "city" VARCHAR(30),
    "send_by_post" BIT,
    "comment" VARCHAR(150),
    "date_created" DATE,
    "is_completed" BIT
);

--RELATIONS

CREATE TABLE "ARTICLE_TAG_IN_ARTICLE" (
    "tag_id" VARCHAR(6) FOREIGN KEY,
    "article_id" VARCHAR(6) FOREIGN KEY
);

CREATE TABLE "PHOTOS_IN_PURCHASE_ORDER" (
    "purchase_order_id" VARCHAR(6) FOREIGN KEY,
    "photo_id" VARCHAR(6) FOREIGN KEY,
    "img_size" VARCHAR(10)
);

CREATE TABLE "PHOTO_TAG_IN_PHOTO" (
    "tag_id" VARCHAR(6) FOREIGN KEY,
    "photo_id" VARCHAR(6) FOREIGN
);



