package no.fg.hilflingbackend

import com.azure.storage.blob.models.PublicAccessType
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.batchInsert
import me.liuwj.ktorm.dsl.insert
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
import no.fg.hilflingbackend.dto.MotiveCreateRequestDto
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotiveId
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerId
import no.fg.hilflingbackend.dto.PhotoGangBangerPositionDto
import no.fg.hilflingbackend.dto.PhotoId
import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlaceId
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.model.PhotoGangBangerToPositions
import no.fg.hilflingbackend.model.Photos
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.CategoryRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.repository.PositionRepository
import no.fg.hilflingbackend.valueobject.Email
import no.fg.hilflingbackend.valueobject.PhoneNumber
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import no.fg.hilflingbackend.valueobject.SemesterStart
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.SecureRandom
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Service
class MockDataService {
  @Autowired lateinit var database: Database

  @Autowired lateinit var photoController: PhotoController

  @Autowired lateinit var placeRepository: PlaceRepository

  @Autowired lateinit var albumRepository: AlbumRepository

  @Autowired lateinit var photoGangBangerRepository: PhotoGangBangerRepository

  @Autowired lateinit var positionRepository: PositionRepository

  @Autowired lateinit var categoryRepository: CategoryRepository

  @Autowired lateinit var eventOwnerRepository: EventOwnerRepository

  @Autowired lateinit var gangRepository: GangRepository

  @Autowired lateinit var motiveRepository: MotiveRepository

  @Autowired lateinit var photoRepository: PhotoRepository

  fun generateSecurityLevelData(): List<SecurityLevelDto> =
    listOf(
      SecurityLevelDto(securityLevelType = SecurityLevelType.FG),
      SecurityLevelDto(securityLevelType = SecurityLevelType.HUSFOLK),
      SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
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
    val seed = UUID.randomUUID().toString()
    return "https://picsum.photos/seed/$seed/1200/800"
  }

  fun generatePhoto(savedMotives: List<MotiveDto>): List<PhotoDto> {
    val list = mutableListOf<PhotoDto>()
    val motives = savedMotives
    val analogMotives = motives.filter { it.analogAlbumDto != null }
    val gangs = generateGangData()
    val photoGangBangers = generatePhotoGangBangerData()
    for (i in 1..1000) {
      val uuid = UUID.randomUUID()
      val url = getPhotoFromApi()
      list.add(
        PhotoDto(
          photoId = PhotoId(uuid),
          goodPicture = listOf(true, false).random(),
          analog = false,
          imageNumber = (i - 1) % 99 + 1,
          pageNumber = (i - 1) / 99 + 1,
          imageProd = url,
          imageWeb = url,
          imageThumb = url,
          motive = motives.random(),
          gang = gangs.random(),
          photoGangBangerDto = photoGangBangers.random(),
          dateTaken = LocalDate.now(),
        ),
      )
    }
    for (i in 1..200) {
      val uuid = UUID.randomUUID()
      val url = getPhotoFromApi()
      list.add(
        PhotoDto(
          photoId = PhotoId(uuid),
          goodPicture = listOf(true, false).random(),
          analog = true,
          imageNumber = (i - 1) % 99 + 1,
          pageNumber = (i - 1) / 99 + 1,
          imageProd = url,
          imageWeb = url,
          imageThumb = url,
          motive = analogMotives.random(),
          gang = gangs.random(),
          photoGangBangerDto = photoGangBangers.random(),
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

  fun generateMotiveData(): List<MotiveCreateRequestDto> =
    listOf(
      MotiveCreateRequestDto(
        title = "Amber Butts spiller på klubben",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = generateAnalogAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 3, 15),
      ),
      MotiveCreateRequestDto(
        title = "High As a Kite 2020",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = generateAnalogAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2020, 2, 8),
      ),
      MotiveCreateRequestDto(
        title = "UKErevyen 2019 Generalprøve",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 10, 12),
      ),
      MotiveCreateRequestDto(
        title = "Storsalen konsert med Karpe",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 11, 23),
      ),
      MotiveCreateRequestDto(
        title = "Fotogjengen gruppebilde Vår 2019",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = generateAnalogAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 4, 5),
      ),
      MotiveCreateRequestDto(
        title = "Lyche Kaffe Bar åpning",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2017, 9, 1),
      ),
      MotiveCreateRequestDto(
        title = "ISFIT Workshop Teknologi",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 2, 14),
      ),
      MotiveCreateRequestDto(
        title = "Rundstyremøte November",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = generateAnalogAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 11, 7),
      ),
      MotiveCreateRequestDto(
        title = "Diversegjengen juleverksted",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 12, 10),
      ),
      MotiveCreateRequestDto(
        title = "Klubbstyret på Daglighallen",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 1, 20),
      ),
      MotiveCreateRequestDto(
        title = "Immatrikulering 2018",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 8, 25),
      ),
      MotiveCreateRequestDto(
        title = "Knaus live på Edgar",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = generateAnalogAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 5, 17),
      ),
      MotiveCreateRequestDto(
        title = "Kulturuka 2019 åpning",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 3, 4),
      ),
      MotiveCreateRequestDto(
        title = "Sangkoret konsert i Bodegaen",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 4, 21),
      ),
      MotiveCreateRequestDto(
        title = "Strossa fredagsfest",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 9, 13),
      ),
      MotiveCreateRequestDto(
        title = "Debatt om studentpolitikk",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = generateAnalogAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 10, 3),
      ),
      MotiveCreateRequestDto(
        title = "Symfonisk Orkester øvelse",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 1, 9),
      ),
      MotiveCreateRequestDto(
        title = "Lørdagspils i Storsalen",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 3, 10),
      ),
      MotiveCreateRequestDto(
        title = "Forestillingsgjengen øving",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 6, 18),
      ),
      MotiveCreateRequestDto(
        title = "Quiz night på Klubben",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = generateAnalogAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 5, 25),
      ),
      MotiveCreateRequestDto(
        title = "DJ Marcus konsert",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 8, 2),
      ),
      MotiveCreateRequestDto(
        title = "Vors på Selskapssiden",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 9, 14),
      ),
      MotiveCreateRequestDto(
        title = "Teaterverkstedet premiere",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 11, 6),
      ),
      MotiveCreateRequestDto(
        title = "Fotballturné på Dragvoll",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = generateAnalogAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 6, 8),
      ),
      MotiveCreateRequestDto(
        title = "Markedssjef presentasjon",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 2, 27),
      ),
      MotiveCreateRequestDto(
        title = "Rått og Rådig release party",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 8, 17),
      ),
      MotiveCreateRequestDto(
        title = "Samfundsmøte Oktober",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 10, 15),
      ),
      MotiveCreateRequestDto(
        title = "KSG Uke på Kino",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = generateAnalogAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 4, 11),
      ),
      MotiveCreateRequestDto(
        title = "Backstreet Boys tribute band",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 7, 20),
      ),
      MotiveCreateRequestDto(
        title = "Eksamensfest Vår 2018",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 5, 30),
      ),
      MotiveCreateRequestDto(
        title = "Lysgruppa rigging i Storsalen",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 1, 28),
      ),
      MotiveCreateRequestDto(
        title = "Filmklubb visning av Blade Runner",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = generateAnalogAlbumData().random(),
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 11, 14),
      ),
      MotiveCreateRequestDto(
        title = "Lysgruppa rigging i Storsalen",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.now(),
      ),
      MotiveCreateRequestDto(
        title = "Akademisk Kor julekonsert",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 12, 18),
      ),
      MotiveCreateRequestDto(
        title = "Stand-up kveld med lokale komikere",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 3, 22),
      ),
      MotiveCreateRequestDto(
        title = "Husfolk møte Mars",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 3, 5),
      ),
      MotiveCreateRequestDto(
        title = "Karaoke Night på Edgar",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 4, 13),
      ),
      MotiveCreateRequestDto(
        title = "Trønder-Rock Festival 2019",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 7, 12),
      ),
      MotiveCreateRequestDto(
        title = "Leder opplæring workshop",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 9, 8),
      ),
      MotiveCreateRequestDto(
        title = "Sommerfest på taket",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 6, 21),
      ),
      MotiveCreateRequestDto(
        title = "Vinkjelleren åpent hus",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 10, 26),
      ),
      MotiveCreateRequestDto(
        title = "Jazzfest i Klubben",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 5, 3),
      ),
      MotiveCreateRequestDto(
        title = "Fotografering av nye gjengmedlemmer",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 8, 28),
      ),
      MotiveCreateRequestDto(
        title = "Brettspillkveld på Daglighallen",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 10, 25),
      ),
      MotiveCreateRequestDto(
        title = "Rockeverkstedet showcase",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 5, 11),
      ),
      MotiveCreateRequestDto(
        title = "Valentinsdag arrangement",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 2, 14),
      ),
      MotiveCreateRequestDto(
        title = "Cocktail-kurs på Knaus",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 11, 29),
      ),
      MotiveCreateRequestDto(
        title = "Dugnadsdag rengjøring",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 8, 24),
      ),
      MotiveCreateRequestDto(
        title = "Arkitektgjengen utstilling",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 4, 27),
      ),
      MotiveCreateRequestDto(
        title = "Spinning Records DJ-kurs",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 9, 19),
      ),
      MotiveCreateRequestDto(
        title = "Huskonsert i Bodegaen",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2018, 3, 16),
      ),
      MotiveCreateRequestDto(
        title = "Podcast opptak Studio 42",
        albumDto = generateAlbumData().random(),
        analogAlbumDto = null,
        eventOwnerDto = generateEventOwnerData().random(),
        categoryDto = generateCategoryData().random(),
        placeDto = generatePlaceData().random(),
        securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
        date = LocalDate.of(2019, 11, 30),
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
        name = "DIGGA",
        description = "Vår 2017",
      ),
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f1")),
        name = "DIGGB",
        description = "Høst 2017",
      ),
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f2")),
        name = "DIGGC",
        description = "Vår 2018",
      ),
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f3")),
        name = "DIGGD",
        description = "Høst 2018",
      ),
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f4")),
        name = "DIGGE",
        description = "Vår 2019",
      ),
      AlbumDto(
        albumId =
          AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f5")),
        name = "DIGGF",
        description = "Høst 2019",
      ),
    )

  fun generateAnalogAlbumData(): List<AlbumDto> =
    listOf(
      AlbumDto(
        albumId = AlbumId(UUID.fromString("a1000000-0000-0000-0000-000000000001")),
        name = "ROLL-001",
        description = "Analog Vår 2017",
        analog = true,
      ),
      AlbumDto(
        albumId = AlbumId(UUID.fromString("a1000000-0000-0000-0000-000000000002")),
        name = "ROLL-002",
        description = "Analog Høst 2017",
        analog = true,
      ),
      AlbumDto(
        albumId = AlbumId(UUID.fromString("a1000000-0000-0000-0000-000000000003")),
        name = "ROLL-003",
        description = "Analog Vår 2018",
        analog = true,
      ),
      AlbumDto(
        albumId = AlbumId(UUID.fromString("a1000000-0000-0000-0000-000000000004")),
        name = "ROLL-004",
        description = "Analog Høst 2018",
        analog = true,
      ),
      AlbumDto(
        albumId = AlbumId(UUID.fromString("a1000000-0000-0000-0000-000000000005")),
        name = "ROLL-005",
        description = "Analog Vår 2019",
        analog = true,
      ),
    )

  fun generatePhotoGangBangerData(): List<PhotoGangBangerDto> =
    listOf(
      PhotoGangBangerDto(
        photoGangBangerId =
          PhotoGangBangerId(
            UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b839"),
          ),
        isActive = true,
        isPang = true,
        semesterStart = SemesterStart("H2018"),
        firstName = "Sindre",
        lastName = "Sivertsen",
        username = "sjsivert",
        email = "emailtest@gmail.com",
        profilePicture = "",
        phoneNumber = "91382506",
      ),
      PhotoGangBangerDto(
        photoGangBangerId =
          PhotoGangBangerId(
            UUID.fromString("7a89444f-25f6-44d9-8a73-94587d72b839"),
          ),
        isActive = true,
        isPang = true,
        semesterStart = SemesterStart("H2018"),
        firstName = "Caroline",
        lastName = "Sandbråten",
        username = "Carossa",
        email = "emailtest@gmail.com",
        profilePicture = "",
        phoneNumber = "91382506",
      ),
    )

  fun generatePhotoGangBangerPositionData(): List<PhotoGangBangerPositionDto> {
    val pgbs = generatePhotoGangBangerData()
    val positions = generatePositionData()
    return listOf(
      PhotoGangBangerPositionDto(
        photoGangBangerId = pgbs[0].photoGangBangerId,
        semesterStart = SemesterStart("H2024"),
        photoGangBangerDto = pgbs[0],
        position = positions[0],
        semesterEnd = null,
      ),
      PhotoGangBangerPositionDto(
        photoGangBangerId = pgbs[1].photoGangBangerId,
        semesterStart = SemesterStart("H2024"),
        photoGangBangerDto = pgbs[1],
        position = positions[1],
        semesterEnd = null,
      ),
    )
  }

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
    println(">>> seedMockData called at " + java.time.Instant.now())
    generateAlbumData().forEach { albumRepository.create(it) }
    generateAnalogAlbumData().forEach { albumRepository.create(it) }
    generatePositionData().forEach { positionRepository.create(it) }
    println("Position seeded")
    println(positionRepository.findAll())

    generatePhotoGangBangerData().forEach { photoGangBangerRepository.create(it) }
    generatePhotoGangBangerPositionData().forEach { pgbPosition ->
      database.insert(PhotoGangBangerToPositions) {
        set(it.photoGangBangerId, pgbPosition.photoGangBangerId.id)
        set(it.positionId, pgbPosition.position.positionId.id)
        set(it.semesterStart, pgbPosition.semesterStart.value)
        set(it.semesterEnd, pgbPosition.semesterEnd?.value)
      }
    }
    println("PhotoGangBangers seeded")
    generateCategoryData().forEach { categoryRepository.create(it) }
    println("Category seeded")

    generateEventOwnerData().forEach { eventOwnerRepository.create(it) }
    generatePlaceData().forEach { placeRepository.create(it) }
    generateGangData().forEach { gangRepository.create(it) }

    val savedMotives = generateMotiveData().map { motiveRepository.create(it) }
    database.batchInsert(Photos) {
      generatePhoto(savedMotives).map { photoDto ->
        item {
          set(it.id, photoDto.photoId.id)
          set(it.goodPicture, photoDto.goodPicture)
          set(it.analog, photoDto.analog)
          set(it.imageNumber, photoDto.imageNumber)
          set(it.pageNumber, photoDto.pageNumber)
          set(it.imageProd, photoDto.imageProd)
          set(it.imageWeb, photoDto.imageWeb)
          set(it.imageThumb, photoDto.imageThumb)
          set(it.securityLevel, photoDto.securityLevel.securityLevelType.type)
          set(it.motiveId, photoDto.motive.motiveId.id)
          set(it.gangId, photoDto.gang?.gangId?.id)
          set(it.photoGangBangerId, photoDto.photoGangBangerDto.photoGangBangerId.id)
        }
      }
    }
    println("Photos Seeded")
  }
}
