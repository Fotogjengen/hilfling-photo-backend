package no.fg.hilflingbackend.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.boolean
import org.ktorm.schema.int

interface PhotoGangBangerPosition : BaseModel<PhotoGangBangerPosition> {
  companion object : Entity.Factory<PhotoGangBangerPosition>()

  var isCurrent: Boolean

  // Foreign keys
  var position: Position
}

object PhotoGangBangerPositions : BaseTable<PhotoGangBangerPosition>("photo_gang_banger_position") {
  val isCurrent = boolean("is_current").bindTo { it.isCurrent }

  // Foreign keys
  val position = int("position_id").references(Positions) { it.position }
}

val Database.photo_gang_banger_positions get() = this.sequenceOf(PhotoGangBangerPositions)
