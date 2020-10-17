package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*

interface PhotoGangBanger : BaseModel<PhotoGangBanger> {
    companion object : Entity.Factory<PhotoGangBanger>()

    var relationshipStatus: String
    var semesterStart: String
    var isActive: Boolean
    var isPang: Boolean
    var address: String
    var zipCode: String
    var city: String

    // From User model
    var samfundetUser: SamfundetUser
}

object PhotoGangBangers : BaseTable<PhotoGangBanger>("photo_gang_banger") {
    val relationshipStatus = varchar("relationship_status").bindTo { it.relationshipStatus }
    val semesterStart = varchar("semester_start").bindTo { it.semesterStart }
    val isActive = boolean("is_active").bindTo { it.isActive }
    val isPang = boolean("is_pang").bindTo { it.isPang }
    val address = varchar("address").bindTo { it.address }
    val zipCode = varchar("zip_code").bindTo { it.zipCode }
    val city = varchar("city").bindTo { it.city }

    // From User model
    val samfundetUserId = int("samfundet_user_id").references(SamfundetUsers){it.samfundetUser}
}

val Database.photo_gang_bangers get() = this.sequenceOf(PhotoGangBangers)