package hilfling.backend.hilfing.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*

interface Photo : BaseModel<Photo> {
    companion object : Entity.Factory<Photo>()

    var isGoodPicture: Boolean

    var smallUrl: String
    var mediumUrl: String
    var largeUrl: String

    // Foreign keys
    var motive: Motive
    var place: Place
    var securityLevel: SecurityLevel
    var gang: Gang
    var photoGangBanger: PhotoGangBanger
}

object Photos : BaseTable<Photo>("photo") {
    val isGoodPicture = boolean("is_good_picture").bindTo { it.isGoodPicture }

    val smallUrl = varchar("small_url").bindTo { it.smallUrl }
    val mediumUrl = varchar("medium_url").bindTo { it.mediumUrl }
    val largeUrl = varchar("large_url").bindTo { it.largeUrl }

    // Foreign keys
    val motive = int("motive_id").references(Motives){it.motive}
    val place = int("place_id").references(Places){it.place}
    val securityLevel = int("security_level_id").references(SecurityLevels){it.securityLevel}
    val gang = int("gang_id").references(Gangs){it.gang}
    val photoGangBanger = int("photo_gang_banger_id").references(PhotoGangBangers){it.photoGangBanger}
}

val Database.photos get() = this.sequenceOf(Photos)