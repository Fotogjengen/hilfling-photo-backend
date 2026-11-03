/* Foreign keys are not indexed automatically; every motive page and album check looks photos up by motive */
CREATE INDEX idx_photo_motive_id ON PHOTO (motive_id);

/* Good pictures feed: paged newest first, tie-broken by id */
CREATE INDEX idx_photo_good_feed ON PHOTO (date_created DESC, id)
    WHERE good_picture AND date_deleted IS NULL;

/* Finding the photos stored in an album */
CREATE INDEX idx_motive_album_id ON MOTIVE (album_id);
CREATE INDEX idx_motive_analog_album_id ON MOTIVE (analog_album_id);
