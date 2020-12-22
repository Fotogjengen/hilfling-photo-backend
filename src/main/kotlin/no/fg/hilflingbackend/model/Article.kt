package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PhotoGangBangerDto

interface Article : BaseModel<Article> {
  companion object : Entity.Factory<Article>()

  var title: String
  var plainText: String

  // Foreign keys
  var securityLevel: SecurityLevel
  var photoGangBanger: PhotoGangBanger
}

object Articles : BaseTable<Article>("article") {
  val title = varchar("title").bindTo { it.title }
  val plainText = varchar("plain_text").bindTo { it.plainText }

  // Foreign keys
  val securityLevelId = int("security_level_id").references(SecurityLevels) { it.securityLevel }
  val photoGangBangerId = int("photo_gang_banger_id").references(PhotoGangBangers) { it.photoGangBanger }
}

val Database.articles get() = this.sequenceOf(Articles)
