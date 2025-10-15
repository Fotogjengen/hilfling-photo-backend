package no.fg.hilflingbackend

import com.azure.storage.blob.models.PublicAccessType
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.batchInsert
import no.fg.hilflingbackend.blobStorage.AzureBlobStorage
import no.fg.hilflingbackend.controller.PhotoController
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumId
import no.fg.hilflingbackend.dto.CategoryDto
import no.fg.hilflingbackend.dto.CategoryId
import no.fg.hilflingbackend.dto.EventOwnerDto
import no.fg.hilflingbackend.dto.EventOwnerId
import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.GangId
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotiveId
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerId
import no.fg.hilflingbackend.dto.PhotoGangBangerPositionDto
import no.fg.hilflingbackend.dto.PhotoGangBangerPositionId
import no.fg.hilflingbackend.dto.PhotoId
import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.dto.PhotoTagId
import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlaceId
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.dto.RelationshipStatus
import no.fg.hilflingbackend.dto.SamfundetUserDto
import no.fg.hilflingbackend.dto.SamfundetUserId
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.dto.SecurityLevelId
import no.fg.hilflingbackend.dto.SemesterStart
import no.fg.hilflingbackend.model.Photos
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.CategoryRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.PhotoTagRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.repository.PositionRepository
import no.fg.hilflingbackend.repository.SamfundetUserRepository
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.PhoneNumber
import no.fg.hilflingbackend.value_object.SecurityLevelType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.SecureRandom
import java.time.LocalDate
import java.util.UUID

@Service
class MockDataService {
  @Autowired lateinit var database: Database

  @Autowired lateinit var photoController: PhotoController

  @Autowired lateinit var placeRepository: PlaceRepository

  @Autowired lateinit var albumRepository: AlbumRepository

  @Autowired lateinit var photoGangBangerRepository: PhotoGangBangerRepository

  @Autowired lateinit var positionRepository: PositionRepository

  @Autowired lateinit var samfundetUserRepository: SamfundetUserRepository

  @Autowired lateinit var photoTagRepository: PhotoTagRepository

  @Autowired lateinit var categoryRepository: CategoryRepository

  @Autowired lateinit var eventOwnerRepository: EventOwnerRepository

  @Autowired lateinit var gangRepository: GangRepository

  @Autowired lateinit var motiveRepository: MotiveRepository

  @Autowired lateinit var securityLevelRepository: SecurityLevelRepository

  @Autowired lateinit var photoRepository: PhotoRepository

  fun generateSecurityLevelData(): List<SecurityLevelDto> =
    listOf(
      SecurityLevelDto(
        securityLevelId =
          SecurityLevelId(
            UUID.fromString("8214142f-7c08-48ad-9130-fd7ac6b23e51"),
          ),
        securityLevelType = SecurityLevelType.FG,
      ),
      SecurityLevelDto(
        securityLevelId =
          SecurityLevelId(
            UUID.fromString("8214142f-7c08-48ad-9130-fd7ac6b23e52"),
          ),
        securityLevelType = SecurityLevelType.HUSFOLK,
      ),
      SecurityLevelDto(
        securityLevelId =
          SecurityLevelId(
            UUID.fromString("8314142f-7c08-48ad-9130-fd7ac6b23e52"),
          ),
        securityLevelType = SecurityLevelType.ALLE,
      ),
    )

  fun generateGangData(): List<GangDto> =
    listOf(
      GangDto(
        gangId = GangId(UUID.fromString("b0bd026f-cc19-4474-989c-aec8d4a76bc9")),
        name = "Fotogjengen",
      ),
      GangDto(
        gangId = GangId(UUID.fromString("b1bd026f-cc19-4474-989c-aec8d4a76bc9")),
        name = "Diversegjengen",
      ),
    )

  fun getPhotoFromApi(): String {
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder().uri(URI.create("https://picsum.photos/1200/800")).build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.headers().allValues("location")[0]
  }

  fun generatePhoto(): List<PhotoDto> {
    val list = mutableListOf<PhotoDto>()

    for (i in 1..1000) {
      val random = SecureRandom()
      val byte = random.generateSeed(i)
      val uuid = UUID.nameUUIDFromBytes(byte)
      val url = getPhotoFromApi()
      val motive = generateMotiveData().random()
      list.add(
        PhotoDto(
          photoId = PhotoId(uuid),
          largeUrl = url,
          motive = motive,
          placeDto = generatePlaceData().random(),
          securityLevel = generateSecurityLevelData().random(),
          gang = generateGangData().random(),
          isGoodPicture = true,
          smallUrl = url,
          mediumUrl = url,
          photoGangBangerDto = generatePhotoGangBangerData().random(),
          photoTags = generatePhotoTagData(),
          albumDto = generateAlbumData().random(),
          categoryDto = generateCategoryData().random(),
          dateTaken = LocalDate.now(),
        ),
      )
    }
    return list
  }

  fun generatePlaceData(): List<PlaceDto> =
    listOf(
      PlaceDto(
        placeId =
          PlaceId(UUID.fromString("9f4fa5d6-ad7c-419c-be58-1ee73f212675")),
        name = "Klubben",
      ),
      PlaceDto(
        placeId =
          PlaceId(UUID.fromString("8f4fa5d6-ad7c-419c-be58-1ee73f212675")),
        name = "Storsalen",
      ),
    )

  fun generateMotiveData(): List<MotiveDto> =
    listOf(
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc4bcd")),
        title = "Amber Butts spiller på klubben",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 3, 15),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc5bcd")),
        title = "High As a Kite 2020",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2020, 2, 8),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("a1540f3c-77b8-4bc5-acc7-4dd7d8cc4bc1")),
        title = "UKErevyen 2019 Generalprøve",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 10, 12),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("a2540f3c-77b8-4bc5-acc7-4dd7d8cc4bc2")),
        title = "Storsalen konsert med Karpe",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 11, 23),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("a3540f3c-77b8-4bc5-acc7-4dd7d8cc4bc3")),
        title = "Fotogjengen gruppebilde Vår 2019",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 4, 5),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("a4540f3c-77b8-4bc5-acc7-4dd7d8cc4bc4")),
        title = "Lyche Kaffe Bar åpning",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2017, 9, 1),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("a5540f3c-77b8-4bc5-acc7-4dd7d8cc4bc5")),
        title = "ISFIT Workshop Teknologi",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 2, 14),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("a6540f3c-77b8-4bc5-acc7-4dd7d8cc4bc6")),
        title = "Rundstyremøte November",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 11, 7),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("a7540f3c-77b8-4bc5-acc7-4dd7d8cc4bc7")),
        title = "Diversegjengen juleverksted",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 12, 10),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("a8540f3c-77b8-4bc5-acc7-4dd7d8cc4bc8")),
        title = "Klubbstyret på Daglighallen",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 1, 20),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("a9540f3c-77b8-4bc5-acc7-4dd7d8cc4bc9")),
        title = "Immatrikulering 2018",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 8, 25),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("b0540f3c-77b8-4bc5-acc7-4dd7d8cc4bd0")),
        title = "Knaus live på Edgar",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 5, 17),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("b1540f3c-77b8-4bc5-acc7-4dd7d8cc4bd1")),
        title = "Kulturuka 2019 åpning",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 3, 4),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("b2540f3c-77b8-4bc5-acc7-4dd7d8cc4bd2")),
        title = "Sangkoret konsert i Bodegaen",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 4, 21),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("b3540f3c-77b8-4bc5-acc7-4dd7d8cc4bd3")),
        title = "Strossa fredagsfest",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 9, 13),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("b4540f3c-77b8-4bc5-acc7-4dd7d8cc4bd4")),
        title = "Debatt om studentpolitikk",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 10, 3),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("b5540f3c-77b8-4bc5-acc7-4dd7d8cc4bd5")),
        title = "Symfonisk Orkester øvelse",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 1, 9),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("b6540f3c-77b8-4bc5-acc7-4dd7d8cc4bd6")),
        title = "Lørdagspils i Storsalen",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 3, 10),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("b7540f3c-77b8-4bc5-acc7-4dd7d8cc4bd7")),
        title = "Forestillingsgjengen øving",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 6, 18),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("b8540f3c-77b8-4bc5-acc7-4dd7d8cc4bd8")),
        title = "Quiz night på Klubben",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 5, 25),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("b9540f3c-77b8-4bc5-acc7-4dd7d8cc4bd9")),
        title = "DJ Marcus konsert",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 8, 2),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("c0540f3c-77b8-4bc5-acc7-4dd7d8cc4be0")),
        title = "Vors på Selskapssiden",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 9, 14),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("c1540f3c-77b8-4bc5-acc7-4dd7d8cc4be1")),
        title = "Teaterverkstedet premiere",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 11, 6),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("c2540f3c-77b8-4bc5-acc7-4dd7d8cc4be2")),
        title = "Fotballturné på Dragvoll",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 6, 8),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("c3540f3c-77b8-4bc5-acc7-4dd7d8cc4be3")),
        title = "Markedssjef presentasjon",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 2, 27),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("c4540f3c-77b8-4bc5-acc7-4dd7d8cc4be4")),
        title = "Rått og Rådig release party",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 8, 17),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("c5540f3c-77b8-4bc5-acc7-4dd7d8cc4be5")),
        title = "Samfundsmøte Oktober",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 10, 15),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("c6540f3c-77b8-4bc5-acc7-4dd7d8cc4be6")),
        title = "KSG Uke på Kino",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 4, 11),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("c7540f3c-77b8-4bc5-acc7-4dd7d8cc4be7")),
        title = "Backstreet Boys tribute band",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 7, 20),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("c8540f3c-77b8-4bc5-acc7-4dd7d8cc4be8")),
        title = "Eksamensfest Vår 2018",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 5, 30),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("c9540f3c-77b8-4bc5-acc7-4dd7d8cc4be9")),
        title = "Lysgruppa rigging i Storsalen",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 1, 28),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("d0540f3c-77b8-4bc5-acc7-4dd7d8cc4bf0")),
        title = "Filmklubb visning av Blade Runner",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 11, 14),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("3a6c1f82-9b74-4d3a-bd2b-8f21c4e99c54")),
        title = "Lysgruppa rigging i Storsalen",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.now(),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("d1540f3c-77b8-4bc5-acc7-4dd7d8cc4bf1")),
        title = "Akademisk Kor julekonsert",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 12, 18),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("d2540f3c-77b8-4bc5-acc7-4dd7d8cc4bf2")),
        title = "Stand-up kveld med lokale komikere",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 3, 22),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("d3540f3c-77b8-4bc5-acc7-4dd7d8cc4bf3")),
        title = "Husfolk møte Mars",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 3, 5),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("d4540f3c-77b8-4bc5-acc7-4dd7d8cc4bf4")),
        title = "Karaoke Night på Edgar",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 4, 13),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("d5540f3c-77b8-4bc5-acc7-4dd7d8cc4bf5")),
        title = "Trønder-Rock Festival 2019",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 7, 12),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("d6540f3c-77b8-4bc5-acc7-4dd7d8cc4bf6")),
        title = "Leder opplæring workshop",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 9, 8),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("d7540f3c-77b8-4bc5-acc7-4dd7d8cc4bf7")),
        title = "Sommerfest på taket",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 6, 21),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("d8540f3c-77b8-4bc5-acc7-4dd7d8cc4bf8")),
        title = "Vinkjelleren åpent hus",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 10, 26),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("d9540f3c-77b8-4bc5-acc7-4dd7d8cc4bf9")),
        title = "Jazzfest i Klubben",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 5, 3),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("e0540f3c-77b8-4bc5-acc7-4dd7d8cc4c00")),
        title = "Fotografering av nye gjengmedlemmer",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 8, 28),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("e1540f3c-77b8-4bc5-acc7-4dd7d8cc4c01")),
        title = "Brettspillkveld på Daglighallen",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 10, 25),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("e2540f3c-77b8-4bc5-acc7-4dd7d8cc4c02")),
        title = "Rockeverkstedet showcase",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 5, 11),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("e3540f3c-77b8-4bc5-acc7-4dd7d8cc4c03")),
        title = "Valentinsdag arrangement",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 2, 14),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("e4540f3c-77b8-4bc5-acc7-4dd7d8cc4c04")),
        title = "Cocktail-kurs på Knaus",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 11, 29),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("e5540f3c-77b8-4bc5-acc7-4dd7d8cc4c05")),
        title = "Dugnadsdag rengjøring",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 8, 24),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("e6540f3c-77b8-4bc5-acc7-4dd7d8cc4c06")),
        title = "Arkitektgjengen utstilling",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 4, 27),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("e7540f3c-77b8-4bc5-acc7-4dd7d8cc4c07")),
        title = "Spinning Records DJ-kurs",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 9, 19),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("e8540f3c-77b8-4bc5-acc7-4dd7d8cc4c08")),
        title = "Huskonsert i Bodegaen",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2018, 3, 16),
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("e9540f3c-77b8-4bc5-acc7-4dd7d8cc4c09")),
        title = "Podcast opptak Studio 42",
        albumDto = generateAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        dateCreated = LocalDate.of(2019, 11, 30),
      ),
    )

  fun generateCategoryData(): List<CategoryDto> =
    listOf(
      CategoryDto(
        categoryId =
          CategoryId(
            UUID.fromString("2832ee5e-3f11-4f11-8189-56ca4f70f418"),
          ),
        name = "Gjengfoto",
      ),
      CategoryDto(
        categoryId =
          CategoryId(
            UUID.fromString("3832ee5e-3f11-4f11-8189-56ca4f70f418"),
          ),
        name = "Konsert",
      ),
    )

  fun generateEventOwnerData(): List<EventOwnerDto> =
    listOf(
      EventOwnerDto(
        eventOwnerId =
          EventOwnerId(
            UUID.fromString("9265f73d-7b13-4673-9f3b-1db3b6c7d526"),
          ),
        name = EventOwnerName.valueOf("Samfundet"),
      ),
      EventOwnerDto(
        eventOwnerId =
          EventOwnerId(
            UUID.fromString("afc308c4-06e2-47bb-b97b-70eb3f55e8d9"),
          ),
        name = EventOwnerName.valueOf("ISFIT"),
      ),
      EventOwnerDto(
        eventOwnerId =
          EventOwnerId(
            UUID.fromString("e91f1201-e0bf-4d25-8026-b2a2d44c37c3"),
          ),
        name = EventOwnerName.valueOf("UKA"),
      ),
    )

  fun generateAlbumData(): List<AlbumDto> =
    listOf(
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("8a2bb663-1260-4c16-933c-a2af7420f5ff")),
        title = "Vår 2017",
      ),
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f1")),
        title = "Høst 2017",
      ),
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f2")),
        title = "Vår 2018",
      ),
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f3")),
        title = "Høst 2018",
      ),
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f4")),
        title = "Vår 2019",
      ),
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f5")),
        title = "Høst 2019",
      ),
    )

  fun generatePhotoTagData(): List<PhotoTagDto> =
    listOf(
      PhotoTagDto(
        photoTagId =
          PhotoTagId(
            UUID.fromString(("d8771ab3-28a9-4b8c-991d-01f6123b8590")),
          ),
        name = "WowFactor100",
      ),
      PhotoTagDto(
        photoTagId =
          PhotoTagId(
            UUID.fromString(("d8771ab3-28a9-4b8c-991d-01f6123b8590")),
          ),
        name = "insane!",
      ),
      PhotoTagDto(
        photoTagId =
          PhotoTagId(
            UUID.fromString(("d8771ab3-28a9-4b8c-991d-01f6123b8590")),
          ),
        name = "Meh",
      ),
    )

  fun generateSamfundetUserData(): List<SamfundetUserDto> =
    listOf(
      SamfundetUserDto(
        samfundetUserId =
          SamfundetUserId(
            UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b822"),
          ),
        email = Email("emailtest@gmail.com"),
        firstName = "Sindre",
        lastName = "Sivertsen",
        profilePicturePath =
          "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        sex = "Mann",
        username = "sjsivert",
        securityLevel = generateSecurityLevelData().random(),
        phoneNumber = PhoneNumber("91382506"),
      ),
      SamfundetUserDto(
        samfundetUserId =
          SamfundetUserId(
            UUID.fromString("7a89444f-25f6-44d9-8a73-94587d72b822"),
          ),
        email = Email("emailtest@gmail.com"),
        firstName = "Caroline",
        lastName = "Sandbråten",
        profilePicturePath =
          "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        sex = "Kvinne",
        username = "Carossa",
        securityLevel = generateSecurityLevelData().random(),
        phoneNumber = PhoneNumber("91382506"),
      ),
    )

  fun generatePhotoGangBangerData(): List<PhotoGangBangerDto> =
    listOf(
      PhotoGangBangerDto(
        photoGangBangerId =
          PhotoGangBangerId(
            UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b839"),
          ),
        address = "Odd Brochmanns Veg 52",
        city = "Trondheim",
        isActive = true,
        isPang = true,
        relationShipStatus = RelationshipStatus.valueOf("single"),
        zipCode = "7051",
        semesterStart = SemesterStart("H2018"),
        samfundetUser = generateSamfundetUserData().random(),
        position = generatePositionData().random(),
      ),
      PhotoGangBangerDto(
        photoGangBangerId =
          PhotoGangBangerId(
            UUID.fromString("7a89444f-25f6-44d9-8a73-94587d72b839"),
          ),
        address = "Odd Brochmanns Veg 52",
        city = "Trondheim",
        isActive = true,
        isPang = true,
        relationShipStatus = RelationshipStatus.single,
        zipCode = "7051",
        semesterStart = SemesterStart("H2018"),
        samfundetUser = generateSamfundetUserData()[1],
        position = generatePositionData()[1],
      ),
    )

  fun generatePhotoGangBangerPositionData(): List<PhotoGangBangerPositionDto> =
    listOf(
      PhotoGangBangerPositionDto(
        photoGangBangerPositionId =
          PhotoGangBangerPositionId(
            UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b831"),
          ),
        isCurrent = true,
        photoGangBangerDto = generatePhotoGangBangerData().random(),
        position = generatePositionData().random(),
      ),
      PhotoGangBangerPositionDto(
        photoGangBangerPositionId =
          PhotoGangBangerPositionId(
            UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b832"),
          ),
        isCurrent = false,
        photoGangBangerDto = generatePhotoGangBangerData().random(),
        position = generatePositionData().random(),
      ),
    )

  fun generatePositionData(): List<PositionDto> =
    listOf(
      PositionDto(
        positionId =
          PositionId(
            (UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f946")),
          ),
        title = "Gjengsjef",
        email = Email("fg-web@samfundet.no"),
      ),
      PositionDto(
        positionId =
          PositionId(
            (UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f945")),
          ),
        title = "Web",
        email = Email("fg-web@samfundet.no"),
      ),
    )

  fun seedMockData() {
    generateAlbumData().forEach { albumRepository.create(it) }
    generatePhotoTagData().forEach {
      // hotoTagRepository.create(it)
    }
    println("PhotoTags Seeded")
    generateSecurityLevelData().forEach { securityLevelRepository.create(it) }
    generateSamfundetUserData().forEach { samfundetUserRepository.create(it) }
    println("SamunfdetUsers seeded")
    println(samfundetUserRepository.findAll().toString())
    generatePositionData().forEach { positionRepository.create(it) }
    generatePhotoTagData().forEach {
      // photoTagRepository.create(it)
    }
    println("Position seeded")
    println(positionRepository.findAll())

    generatePhotoGangBangerData().forEach { photoGangBangerRepository.create(it) }
    println("PhotoGangBangers seeded")
    generateCategoryData().forEach { categoryRepository.create(it) }
    println("Category seeded")

    generateEventOwnerData().forEach { eventOwnerRepository.create(it) }

    generateMotiveData().forEach { motiveRepository.create(it) }
    generatePlaceData().forEach { placeRepository.create(it) }
    generateGangData().forEach { gangRepository.create(it) }
    database.batchInsert(Photos) {
      generatePhoto().map { photoDto ->
        item {
          set(it.isGoodPicture, photoDto.isGoodPicture)
          set(it.id, photoDto.photoId.id)
          set(it.largeUrl, photoDto.largeUrl)
          set(it.motiveId, photoDto.motive.motiveId.id)
          set(it.securityLevelId, photoDto.securityLevel.securityLevelId.id)
          set(it.gangId, photoDto.gang?.gangId?.id)
          set(it.placeId, photoDto.placeDto.placeId.id)
          set(it.smallUrl, photoDto.smallUrl)
          set(it.mediumUrl, photoDto.mediumUrl)
          set(it.photoGangBangerId, photoDto.photoGangBangerDto.photoGangBangerId.id)
          set(it.albumId, photoDto.albumDto.albumId.id)
          set(it.categoryId, photoDto.categoryDto.categoryId.id)
        }
      }
    }
    println("Photos Seeded")
  }
}
