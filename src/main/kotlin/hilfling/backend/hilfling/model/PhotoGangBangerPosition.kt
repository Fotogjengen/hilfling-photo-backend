package hilfling.backend.hilfing.model

import hilfling.backend.hilfing.model.Photos.references
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*

interface PhotoGangBangerPosition : BaseModel<PhotoGangBangerPosition> {
    companion object : Entity.Factory<PhotoGangBangerPosition>()

    var isCurrent: Boolean

    // Foreign keys
    var position: Position
    var photoGangBanger: PhotoGangBanger
}

object PhotoGangBangerPositions : BaseTable<PhotoGangBangerPosition>("photo_gang_banger_position") {
    val isCurrent = boolean("is_current").bindTo { it.isCurrent }

    // Foreign keys
    val position = int("position_id").references(Positions){it.position}
    val photoGangBanger = int("photo_gang_banger_id").references(PhotoGangBangers){it.photoGangBanger}

}

val Database.photo_gang_banger_positions get() = this.sequenceOf(PhotoGangBangerPositions)