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
import no.fg.hilflingbackend.dto.MotiveCreateRequestDto
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotiveId
import no.fg.hilflingbackend.dto.MotivePatchRequestDto
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerId
import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlaceId
import no.fg.hilflingbackend.dto.PlacePatchRequestDto
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.dto.PositionPatchRequestDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.valueobject.SemesterStart
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.CategoryRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.repository.PositionRepository
import no.fg.hilflingbackend.valueobject.Email
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
// import org.junit.runner.RunWith
// import org.springframework.test.context.junit4.SpringRunner

// @RunWith(SpringRunner::class)
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatchIntegrationTest {
  @Autowired
  lateinit var albumRepository: AlbumRepository

  @Autowired
  lateinit var categoryRepository: CategoryRepository

  @Autowired
  lateinit var eventOwnerRepository: EventOwnerRepository

  @Autowired
  lateinit var gangRepository: GangRepository

  @Autowired
  lateinit var positionRepository: PositionRepository

  @Autowired
  lateinit var placeRepository: PlaceRepository

  @Autowired
  lateinit var photoGangBangerRepository: PhotoGangBangerRepository

  @Autowired
  lateinit var motiveRepository: MotiveRepository

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
  lateinit var createdMotiveId1: MotiveId
  final val eventOwnerId1 = EventOwnerId()
  final val eventOwnerId2 = EventOwnerId()
  final val gangId1 = GangId()
  final val gangId2 = GangId()
  final val gangDto1 =
    GangDto(
      gangId = gangId1,
      name = "gang name 1",
    )
  final val gangDto2 =
    GangDto(
      gangId = gangId2,
      name = "gang name 2",
    )
  final val positionDto1 =
    PositionDto(
      positionId = positionId1,
      title = "position title 1",
      email = Email("position1@email.com"),
    )
  final val positionDto2 =
    PositionDto(
      positionId = positionId2,
      title = "position title 2",
      email = Email("position2@email.com"),
    )
  final val placeDto1 =
    PlaceDto(
      placeId = placeId1,
      name = "place name 1",
    )
  final val placeDto2 =
    PlaceDto(
      placeId = placeId2,
      name = "place name 2",
    )
  final val securityLevelDto1 = SecurityLevelDto(securityLevelType = SecurityLevelType.FG)
  final val securityLevelDto2 = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE)
  final val albumDto1 =
    AlbumDto(
      albumId = albumId1,
      name = "DIGGA",
      description = "album title 1",
    )
  final val albumDto2 =
    AlbumDto(
      albumId = albumId2,
      name = "DIGGB",
      description = "album title 2",
    )
  final val categoryDto1 =
    CategoryDto(
      categoryId = categoryId1,
      name = "category name 1",
    )
  final val categoryDto2 =
    CategoryDto(
      categoryId = categoryId2,
      name = "category name 2",
    )
  final val eventOwnerDto1 =
    EventOwnerDto(
      eventOwnerId = eventOwnerId1,
      name = EventOwnerName.Samfundet,
    )
  final val eventOwnerDto2 =
    EventOwnerDto(
      eventOwnerId = eventOwnerId2,
      name = EventOwnerName.ISFIT,
    )
  final val photoGangBangerDto1 =
    PhotoGangBangerDto(
      photoGangBangerId = photoGangBangerId1,
      semesterStart = SemesterStart.invoke("H2019"),
      isActive = true,
      isPang = true,
      firstName = "Caroline",
      lastName = "Sandsbråten",
      username = "carosa",
      email = "mymail@samfundet.no",
      profilePicture = "https://static.independent.co.uk/2021/12/07/10/PRI213893584.jpg",
      phoneNumber = "22225555",
    )
  final val photoGangBangerDto2 =
    PhotoGangBangerDto(
      photoGangBangerId = photoGangBangerId2,
      semesterStart = SemesterStart.invoke("H2018"),
      isActive = true,
      isPang = true,
      firstName = "Sindre",
      lastName = "Sivertsen",
      username = "sinsiv",
      email = "sindre@samfundet.no",
      profilePicture = "https://static.independent.co.uk/2021/12/07/10/PRI213893584.jpg",
      phoneNumber = "12345678",
    )
  final val motiveDto1 =
    MotiveCreateRequestDto(
      title = "motive title 1",
      date = LocalDate.now(),
      categoryDto = categoryDto1,
      eventOwnerDto = eventOwnerDto1,
      placeDto = placeDto1,
      securityLevel = securityLevelDto1,
      albumDto = albumDto1,
      analogAlbumDto = null,
    )
  final val motiveDto2 =
    MotiveCreateRequestDto(
      title = "motive title 2",
      date = LocalDate.now(),
      categoryDto = categoryDto2,
      eventOwnerDto = eventOwnerDto2,
      placeDto = placeDto2,
      securityLevel = securityLevelDto2,
      albumDto = albumDto2,
      analogAlbumDto = null,
    )

  @BeforeAll
  fun fillDb() {
    gangRepository.create(gangDto1)
    gangRepository.create(gangDto2)
    positionRepository.create(positionDto1)
    positionRepository.create(positionDto2)
    placeRepository.create(placeDto1)
    placeRepository.create(placeDto2)
    albumRepository.create(albumDto1)
    albumRepository.create(albumDto2)
    categoryRepository.create(categoryDto1)
    categoryRepository.create(categoryDto2)
    eventOwnerRepository.create(eventOwnerDto1)
    eventOwnerRepository.create(eventOwnerDto2)
    photoGangBangerRepository.create(photoGangBangerDto1)
    photoGangBangerRepository.create(photoGangBangerDto2)
    createdMotiveId1 = motiveRepository.create(motiveDto1).motiveId
    motiveRepository.create(motiveDto2)
  }

  @Test
  fun shouldPatchPosition() {
    val change =
      PositionPatchRequestDto(
        positionId = positionId1,
        title = "changed",
        email = Email("changed@email.com"),
      )
    positionRepository.patch(change)

    val changedFromDb =
      positionRepository.findById(
        positionId1.id,
      )

    assertAll(
      "patch position",
      { assertNotNull(changedFromDb) },
      { assertEquals(changedFromDb?.title, change.title) },
      { assertEquals(changedFromDb?.email, change.email) },
    )
  }

  @Test
  fun shouldPatchPlace() {
    val change =
      PlacePatchRequestDto(
        placeId = placeId1,
        name = "changed",
      )
    placeRepository.patch(change)

    val changedFromDb =
      placeRepository.findById(
        placeId1.id,
      )

    assertAll(
      "patch place",
      { assertNotNull(changedFromDb) },
      { assertEquals(changedFromDb?.name, change.name) },
    )
  }

  @Test
  fun shouldPatchMotive() {
    val change =
      MotivePatchRequestDto(
        motiveId = createdMotiveId1,
        title = "new title hehe",
        date = null,
        categoryDto = categoryDto2,
        eventOwnerDto = eventOwnerDto2,
        placeDto = null,
        securityLevel = null,
        albumDto = albumDto2,
        analogAlbumDto = null,
      )
    motiveRepository.patch(change)
    val changedFromDb = motiveRepository.findById(createdMotiveId1.id)

    assertAll(
      "motive patch",
      { assertNotNull(changedFromDb) },
      { assertEquals(change.title, changedFromDb?.title) },
    )
  }

  @Test
  fun shouldPatchCategory() {
    val change =
      CategoryPatchRequestDto(
        categoryId = categoryId1,
        name = "SAY WHAT",
      )
    categoryRepository.patch(change)
    val changedFromDb = categoryRepository.findById(change.categoryId.id)

    assertAll(
      "category patch",
      { assertNotNull(changedFromDb) },
      { assertEquals(change.name, changedFromDb?.name) },
    )
  }

  @Test
  fun shouldPatchAlbum() {
    val change =
      AlbumPatchRequestDto(
        albumId = albumId1,
        name = "DIGGZ",
        description = "CAROLINE",
        analog = true,
      )
    albumRepository.patch(change)
    val changedFromDb = albumRepository.findById(change.albumId.id)

    assertAll(
      "album patch",
      { assertNotNull(changedFromDb) },
      { assertEquals(change.name, changedFromDb?.name) },
      { assertEquals(change.analog, changedFromDb?.analog) },
    )
  }
}
