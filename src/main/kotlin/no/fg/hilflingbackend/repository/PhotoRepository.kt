package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.asc
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.desc
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.inList
import me.liuwj.ktorm.dsl.innerJoin
import me.liuwj.ktorm.dsl.isNull
import me.liuwj.ktorm.dsl.map
import me.liuwj.ktorm.dsl.or
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.drop
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sorted
import me.liuwj.ktorm.entity.sortedByDescending
import me.liuwj.ktorm.entity.take
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.model.Motives
import no.fg.hilflingbackend.model.Photos
import no.fg.hilflingbackend.model.photos
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
open class PhotoRepository(
  val database: Database,
) {
  fun createPhoto(photo: PhotoDto): PhotoDto {
    database.photos.add(photo.toEntity())
    return photo
  }

  fun findById(
    id: UUID,
    userSecurityLevel: SecurityLevelType,
  ): PhotoDto? {
    val photo = database.photos.find { (it.id eq id) and it.dateDeleted.isNull() }?.toDto() ?: return null
    val canAccessProd = userSecurityLevel.ordinal <= SecurityLevelType.HUSFOLK.ordinal
    return if (canAccessProd) photo else photo.copy(imageProd = null)
  }

  fun deletePhoto(id: UUID) {
    database.update(Photos) {
      set(it.dateDeleted, LocalDate.now())
      where { it.id eq id }
    }
  }

  fun hardDeletePhoto(id: UUID) {
    database.delete(Photos) { it.id eq id }
  }

  fun findByMotiveId(
    motiveId: UUID,
    userSecurityLevel: SecurityLevelType,
  ): List<PhotoDto> {
    val canAccessProd = userSecurityLevel.ordinal <= SecurityLevelType.HUSFOLK.ordinal
    return database.photos
      .filter { (it.motiveId eq motiveId) and it.dateDeleted.isNull() }
      .sortedByDescending { it.dateCreated }
      .toList()
      .map { photo ->
        val dto = photo.toDto()
        if (canAccessProd) dto else dto.copy(imageProd = null)
      }
  }

  fun hasNonDeletedPhotos(motiveId: UUID): Boolean =
    database.photos
      .filter { (it.motiveId eq motiveId) and it.dateDeleted.isNull() }
      .toList()
      .isNotEmpty()

  fun findLastByAlbum(albumId: UUID): Pair<Int, Int>? =
    database
      .from(Photos)
      .innerJoin(Motives, on = Photos.motiveId eq Motives.id)
      .select(Photos.pageNumber, Photos.imageNumber)
      .where { (Motives.albumId eq albumId) or (Motives.analogAlbumId eq albumId) }
      .map { row -> row[Photos.pageNumber]!! to row[Photos.imageNumber]!! }
      .maxByOrNull { (page, image) -> page * 100 + image }

  fun findGoodPicturesByMotiveId(
    motiveId: UUID,
    userSecurityLevel: SecurityLevelType,
  ): List<PhotoDto> {
    val canAccessProd = userSecurityLevel.ordinal <= SecurityLevelType.HUSFOLK.ordinal
    return database.photos
      .filter { (it.motiveId eq motiveId) and (it.goodPicture eq true) and it.dateDeleted.isNull() }
      .sortedByDescending { it.dateCreated }
      .toList()
      .map { photo ->
        val dto = photo.toDto()
        if (canAccessProd) dto else dto.copy(imageProd = null)
      }
  }

  fun findGoodPicturesPage(
    page: Int,
    pageSize: Int,
    userSecurityLevel: SecurityLevelType,
  ): Page<PhotoDto> {
    val canAccessProd = userSecurityLevel.ordinal <= SecurityLevelType.HUSFOLK.ordinal
    val allowedSecurityLevels =
      SecurityLevelType
        .values()
        .filter { it.ordinal >= userSecurityLevel.ordinal }
        .map { it.name }
    val sequence =
      database.photos
        .filter {
          (it.goodPicture eq true) and
            it.dateDeleted.isNull() and
            (it.securityLevel inList allowedSecurityLevels)
        }
    val photos =
      sequence
        .sorted { listOf(it.dateCreated.desc(), it.id.asc()) }
        .drop(page * pageSize)
        .take(pageSize)
        .toList()
        .map { photo ->
          val dto = photo.toDto()
          if (canAccessProd) dto else dto.copy(imageProd = null)
        }
    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = sequence.totalRecords,
      currentList = photos,
    )
  }

  fun updateGoodPicture(
    id: UUID,
    goodPicture: Boolean,
  ) {
    database.update(Photos) {
      set(it.goodPicture, goodPicture)
      where { it.id eq id }
    }
  }
}
