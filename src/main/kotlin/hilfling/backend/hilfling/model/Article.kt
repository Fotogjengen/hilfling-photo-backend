package hilfling.backend.hilfing.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*

interface Article : BaseModel<Article> {
    companion object : Entity.Factory<Article>()

    var title: String
    var content: String

    // Foreign keys
    var securityLevel: SecurityLevel
    var photoGangBanger: PhotoGangBanger
}

object Articles : BaseTable<Article>("article") {
    val title = varchar("name").bindTo { it.title }
    val content = varchar("content").bindTo { it.content }

    // Foreign keys
    val securityLevel = int("security_level_id").references(SecurityLevels){it.securityLevel}
    val photoGangBanger = int("photo_gang_banger_id").references(PhotoGangBangers){it.photoGangBanger}
}

val Database.articles get() = this.sequenceOf(Articles)