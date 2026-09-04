package no.fg.hilflingbackend.valueobject

enum class Permission {
  PHOTO_DELETE_OLD, // delete photos older than 30 days
  ALBUM_MANAGE, // create, update, delete albums
  ARCHIVE_MANAGE, // create, update, delete categories, places, etc.
  POSITION_MANAGE, // create, update, delete positions, add permissions to positions
  USER_MANAGE, // update users, add/remove positions
}
