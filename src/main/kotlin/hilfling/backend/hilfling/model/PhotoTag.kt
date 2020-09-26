package hilfling.backend.hilfing.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*

interface PhotoTag : BaseModel<PhotoTag> {
    companion object : Entity.Factory<PhotoTag>()

    var name: String
}

object PhotoTags : BaseTable<PhotoTag>("photo_tag") {
    val name = varchar("name").bindTo { it.name }
}

val Database.photo_tags get() = this.sequenceOf(PhotoTags)