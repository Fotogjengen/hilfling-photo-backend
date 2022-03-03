package no.fg.hilflingbackend.integrationTests

import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumId
import no.fg.hilflingbackend.dto.AlbumPatchRequestDto
import no.fg.hilflingbackend.dto.CategoryDto
import no.fg.hilflingbackend.dto.CategoryId
import no.fg.hilflingbackend.dto.CategoryPatchRequestDto
import no.fg.hilflingbackend.dto.EventOwnerDto
import no.fg.hilflingbackend.dto.EventOwnerId
import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.GangId
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotiveId
import no.fg.hilflingbackend.dto.MotivePatchRequestDto
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerId
import no.fg.hilflingbackend.dto.PhotoGangBangerPositionPatchRequestDto
import no.fg.hilflingbackend.dto.PhotoId
import no.fg.hilflingbackend.dto.PhotoPatchRequestDto
import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.dto.PhotoTagId
import no.fg.hilflingbackend.dto.PhotoTagPatchRequestDto
import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlaceId
import no.fg.hilflingbackend.dto.PlacePatchRequestDto
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.dto.PositionPatchRequestDto
import no.fg.hilflingbackend.dto.RelationshipStatus
import no.fg.hilflingbackend.dto.SamfundetUserDto
import no.fg.hilflingbackend.dto.SamfundetUserId
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.dto.SecurityLevelId
import no.fg.hilflingbackend.dto.SecurityLevelPatchRequestDto
import no.fg.hilflingbackend.dto.SemesterStart
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.CategoryRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoTagRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.repository.PositionRepository
import no.fg.hilflingbackend.repository.SamfundetUserRepository
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import no.fg.hilflingbackend.service.PhotoService
import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.PhoneNumber
import no.fg.hilflingbackend.value_object.SecurityLevelType
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringRunner
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PatchIntegrationTest() {
  @Autowired
  lateinit var photoService: PhotoService
  @Autowired
  lateinit var albumRepository: AlbumRepository
  @Autowired
  lateinit var categoryRepository: CategoryRepository
  @Autowired
  lateinit var eventOwnerRepository: EventOwnerRepository
  @Autowired
  lateinit var gangRepository: GangRepository
  @Autowired
  lateinit var securityLevelRepository: SecurityLevelRepository
  @Autowired
  lateinit var positionRepository: PositionRepository
  @Autowired
  lateinit var placeRepository: PlaceRepository
  @Autowired
  lateinit var photoTagRepository: PhotoTagRepository
  @Autowired
  lateinit var samfundetUserRepository: SamfundetUserRepository
  @Autowired
  lateinit var photoGangBangerRepository: PhotoGangBangerRepository
  @Autowired
  lateinit var motiveRepository: MotiveRepository

  lateinit var createdPhotoId: UUID
  val mockMultiPartFile = MockMultipartFile("file", "test.png", "text/plain", "some xml".toByteArray())

  final val securityLevelId1 = SecurityLevelId()
  final val securityLevelId2 = SecurityLevelId()
  final val placeId1 = PlaceId()
  final val placeId2 = PlaceId()
  final val albumId1 = AlbumId()
  final val albumId2 = AlbumId()
  final val photoGangBangerId1 = PhotoGangBangerId()
  final val photoGangBangerId2 = PhotoGangBangerId()
  final val categoryId1 = CategoryId()
  final val categoryId2 = CategoryId()
  final val positionId1 = PositionId()
  final val positionId2 = PositionId()
  final val samfundetUserId1 = SamfundetUserId()
  final val samfundetUserId2 = SamfundetUserId()
  final val motiveId1 = MotiveId()
  final val motiveId2 = MotiveId()
  final val eventOwnerId1 = EventOwnerId()
  final val eventOwnerId2 = EventOwnerId()
  final val gangId1 = GangId()
  final val gangId2 = GangId()
  final val photoTagId1 = PhotoTagId()
  final val photoTagId2 = PhotoTagId()

  final val photoTagDto1 = PhotoTagDto(
    photoTagId = photoTagId1,
    name = "photo tag 1"
  )
  final val photoTagDto2 = PhotoTagDto(
    photoTagId = photoTagId2,
    name = "photo tag 2"
  )
  final val gangDto1 = GangDto(
    gangId = gangId1,
    name = "gang name 1"
  )
  final val gangDto2 = GangDto(
    gangId = gangId2,
    name = "gang name 2"
  )
  final val positionDto1 = PositionDto(
    positionId = positionId1,
    title = "position title 1",
    email = Email("position1@email.com")
  )
  final val positionDto2 = PositionDto(
    positionId = positionId2,
    title = "position title 2",
    email = Email("position2@email.com")
  )
  final val placeDto1 = PlaceDto(
    placeId = placeId1,
    name = "place name 1"
  )
  final val placeDto2 = PlaceDto(
    placeId = placeId2,
    name = "place name 2"
  )
  final val securityLevelDto1 = SecurityLevelDto(
    securityLevelId = securityLevelId1,
    securityLevelType = SecurityLevelType.FG
  )
  final val securityLevelDto2 = SecurityLevelDto(
    securityLevelId = securityLevelId2,
    securityLevelType = SecurityLevelType.ALLE
  )
  final val albumDto1 = AlbumDto(
    albumId = albumId1,
    title = "album title 1"
  )
  final val albumDto2 = AlbumDto(
    albumId = albumId2,
    title = "album title 2"
  )
  final val categoryDto1 = CategoryDto(
    categoryId = categoryId1,
    name = "category name 1"
  )
  final val categoryDto2 = CategoryDto(
    categoryId = categoryId2,
    name = "category name 2"
  )
  final val eventOwnerDto1 = EventOwnerDto(
    eventOwnerId = eventOwnerId1,
    name = EventOwnerName.Samfundet
  )
  final val eventOwnerDto2 = EventOwnerDto(
    eventOwnerId = eventOwnerId2,
    name = EventOwnerName.ISFIT
  )
  final val samfundetUserDto1 = SamfundetUserDto(
    samfundetUserId = samfundetUserId1,
    firstName = "Caroline",
    lastName = "Sandsbråten",
    username = "carosa",
    phoneNumber = PhoneNumber("22225555"),
    email = Email("mymail@samfundet.no"),
    profilePicturePath = "https://static.independent.co.uk/2021/12/07/10/PRI213893584.jpg?quality=75&width=982&height=726&auto=webp",
    sex = "Ja",
    securityLevel = securityLevelDto1
  )
  final val samfundetUserDto2 = SamfundetUserDto(
    samfundetUserId = samfundetUserId2,
    firstName = "Sindre",
    lastName = "Sivertsen",
    username = "sinsiv",
    phoneNumber = PhoneNumber("12345678"),
    email = Email("sindre@samfundet.no"),
    profilePicturePath = "https://static.independent.co.uk/2021/12/07/10/PRI213893584.jpg?quality=75&width=982&height=726&auto=webp",
    sex = "Ja",
    securityLevel = securityLevelDto2
  )
  final val photoGangBangerDto1 = PhotoGangBangerDto(
    photoGangBangerId = photoGangBangerId1,
    relationShipStatus = RelationshipStatus.married,
    semesterStart = SemesterStart.invoke("H2019"),
    isActive = true,
    isPang = true,
    address = "Gang banger address",
    position = positionDto1,
    zipCode = "1476",
    city = "Trondheim",
    samfundetUser = samfundetUserDto1
  )
  final val photoGangBangerDto2 = PhotoGangBangerDto(
    photoGangBangerId = photoGangBangerId2,
    relationShipStatus = RelationshipStatus.relationship,
    semesterStart = SemesterStart.invoke("H2018"),
    isActive = true,
    isPang = true,
    address = "Gang banger address 2",
    position = positionDto2,
    zipCode = "1473",
    city = "Trondheim 2",
    samfundetUser = samfundetUserDto2
  )
  final val motiveDto1 = MotiveDto(
    motiveId = motiveId1,
    title = "motive title 1",
    categoryDto = categoryDto1,
    eventOwnerDto = eventOwnerDto1,
    albumDto = albumDto1
  )
  final val motiveDto2 = MotiveDto(
    motiveId = motiveId2,
    title = "motive title 2",
    categoryDto = categoryDto2,
    eventOwnerDto = eventOwnerDto2,
    albumDto = albumDto2
  )

  @BeforeAll
  fun fill_db() {
    photoTagRepository.create(photoTagDto1)
    photoTagRepository.create(photoTagDto2)
    gangRepository.create(gangDto1)
    gangRepository.create(gangDto2)
    positionRepository.create(positionDto1)
    positionRepository.create(positionDto2)
    placeRepository.create(placeDto1)
    placeRepository.create(placeDto2)
    securityLevelRepository.create(securityLevelDto1)
    securityLevelRepository.create(securityLevelDto2)
    albumRepository.create(albumDto1)
    albumRepository.create(albumDto2)
    categoryRepository.create(categoryDto1)
    categoryRepository.create(categoryDto2)
    eventOwnerRepository.create(eventOwnerDto1)
    eventOwnerRepository.create(eventOwnerDto2)
    samfundetUserRepository.create(samfundetUserDto1)
    samfundetUserRepository.create(samfundetUserDto2)
    photoGangBangerRepository.create(photoGangBangerDto1)
    photoGangBangerRepository.create(photoGangBangerDto2)
    motiveRepository.create(motiveDto1)
    motiveRepository.create(motiveDto2)

    createdPhotoId = UUID.fromString(
      photoService.createNewMotiveAndSaveDigitalPhotos(
        motiveString = motiveDto1.title,
        placeString = placeDto1.name,
        eventOwnerString = eventOwnerDto1.name.eventOwnerName,
        securityLevelId = securityLevelId1.id,
        albumId = albumId1.id,
        photoGangBangerId = photoGangBangerId1.id,
        photoFileList = listOf(mockMultiPartFile),
        tagList = listOf(listOf(photoTagDto1.name)),
        categoryName = categoryDto1.name,
        isGoodPhotoList = listOf(true)
      )[0].split("/").last().split(".").first()
    )
  }

  @Test
  fun shouldPatchSecurityLevel() {
    val change = SecurityLevelPatchRequestDto(
      securityLevelId = securityLevelId1,
      securityLevelType = SecurityLevelType.FG
    )
    securityLevelRepository.patch(change)

    val changedFromDb = securityLevelRepository.findById(
      securityLevelId1.id
    )

    assertAll(
      "patch SecurityLevel",
      { assertNotNull(changedFromDb) },
      { assertEquals(changedFromDb?.securityLevelType, change.securityLevelType) }
    )
  }

  @Test
  fun shouldPatchPosition() {
    val change = PositionPatchRequestDto(
      positionId = positionId1,
      title = "changed",
      email = Email("changed@email.com")
    )
    positionRepository.patch(change)

    val changedFromDb = positionRepository.findById(
      positionId1.id
    )

    assertAll(
      "patch position",
      { assertNotNull(changedFromDb) },
      { assertEquals(changedFromDb?.title, change.title) },
      { assertEquals(changedFromDb?.email, change.email) }
    )
  }

  @Test
  fun shouldPatchPlace() {
    val change = PlacePatchRequestDto(
      placeId = placeId1,
      name = "changed"
    )
    placeRepository.patch(change)

    val changedFromDb = placeRepository.findById(
      placeId1.id
    )

    assertAll(
      "patch place",
      { assertNotNull(changedFromDb) },
      { assertEquals(changedFromDb?.name, change.name) }
    )
  }

  @Test
  fun shouldPatchPhotoTag() {
    val change = PhotoTagPatchRequestDto(
      photoTagId = photoTagId1,
      name = "changed"
    )
    photoTagRepository.patch(change)

    val changedFromDb = photoTagRepository.findById(
      photoTagId1.id
    )

    assertAll(
      "patch PhotoTag",
      { assertNotNull(changedFromDb) },
      { assertEquals(changedFromDb?.name, change.name) }
    )
  }

  @Test
  fun shouldPatchPhoto() {
    val photoTags = listOf(
      photoTagRepository.findById(photoTagId1.id)?.name ?: "got no1",
      photoTagRepository.findById(photoTagId2.id)?.name ?: "got no2"
    )
    val change = PhotoPatchRequestDto(
      photoId = PhotoId(createdPhotoId),
      isGoodPicture = false,
      motive = motiveDto2,
      placeDto = placeDto2,
      securityLevel = securityLevelDto2,
      gang = gangDto2,
      albumDto = albumDto2,
      categoryDto = categoryDto2,
      photoGangBangerDto = photoGangBangerDto2,
      photoTags = photoTags
    )

    photoService.patch(change)

    val changedFromDb = photoService.findById(createdPhotoId)

    assertAll(
      "photo patch",
      { assertNotNull(changedFromDb) },
      { assertFalse(changedFromDb.isGoodPicture) },
      { assertEquals(changedFromDb.motive.motiveId.id, motiveDto2.motiveId.id) },
      { assertEquals(changedFromDb.placeDto.placeId.id, placeDto2.placeId.id) },
      { assertEquals(changedFromDb.securityLevel.securityLevelId.id, securityLevelDto2.securityLevelId.id) },
      { assertEquals(changedFromDb.gang?.gangId?.id, gangDto2.gangId.id) },
      { assertEquals(changedFromDb.albumDto.albumId.id, albumDto2.albumId.id) },
      { assertEquals(changedFromDb.categoryDto.categoryId.id, categoryDto2.categoryId.id) },
      { assertEquals(changedFromDb.photoGangBangerDto.photoGangBangerId.id, photoGangBangerDto2.photoGangBangerId.id) },
      { assertEquals(changedFromDb.photoTags.size, 2) },
      { assertEquals(changedFromDb.photoTags[0].photoTagId.id, photoTagDto1.photoTagId.id) },
      { assertEquals(changedFromDb.photoTags[1].photoTagId.id, photoTagDto2.photoTagId.id) }
    )
  }

  @Test
  fun shouldPatchMotive() {
    val change = MotivePatchRequestDto(
      motiveId = motiveId1,
      title = "new title hehe",
      categoryDto = categoryDto2,
      eventOwnerDto = eventOwnerDto2,
      albumDto = albumDto2
    )
    motiveRepository.patch(change)
    val changedFromDb = motiveRepository.findById(motiveId1.id)

    assertAll(
      "motive patch",
      { assertNotNull(changedFromDb) },
      { assertEquals(change.title, changedFromDb?.title) }
    )
  }

  @Test
  fun shouldPatchCategory() {
    val change = CategoryPatchRequestDto(
      categoryId=categoryId1,
      name="SAY WHAT"
    )
    categoryRepository.patch(change)
    val changedFromDb = categoryRepository.findById(change.categoryId.id)

    assertAll(
      "category patch",
      { assertNotNull(changedFromDb) },
      { assertEquals(change.name, changedFromDb?.name) }
    )
  }

  @Test
  fun shouldPatchAlbum() {
    val change = AlbumPatchRequestDto(
      albumId=albumId1,
      title="CAROLINE",
      isAnalog = true
    )
    albumRepository.patch(change)
    val changedFromDb = albumRepository.findById(change.albumId.id)

    assertAll(
      "album patch",
      { assertNotNull(changedFromDb) },
      { assertEquals(change.title, changedFromDb?.title) },
      { assertEquals(change.isAnalog, changedFromDb?.isAnalog) }
      )
  }
}
