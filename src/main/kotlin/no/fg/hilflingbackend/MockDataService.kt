package no.fg.hilflingbackend

import com.azure.storage.blob.models.PublicAccessType
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.batchInsert
import no.fg.hilflingbackend.blobStorage.AzureBlobStorage
import no.fg.hilflingbackend.controller.PhotoController
import no.fg.hilflingbackend.dto.*
import no.fg.hilflingbackend.model.Photos
import no.fg.hilflingbackend.repository.*
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
import java.util.*

@Service
class MockDataService {

  @Autowired
  lateinit var database: Database

  @Autowired
  lateinit var photoController: PhotoController

  @Autowired
  lateinit var placeRepository: PlaceRepository

  @Autowired
  lateinit var albumRepository: AlbumRepository

  @Autowired
  lateinit var photoGangBangerRepository: PhotoGangBangerRepository

  @Autowired
  lateinit var positionRepository: PositionRepository

  @Autowired
  lateinit var samfundetUserRepository: SamfundetUserRepository

  @Autowired
  lateinit var photoTagRepository: PhotoTagRepository

  @Autowired
  lateinit var articleTagRepository: ArticleTagRepository

  @Autowired
  lateinit var categoryRepository: CategoryRepository

  @Autowired
  lateinit var eventOwnerRepository: EventOwnerRepository

  @Autowired
  lateinit var gangRepository: GangRepository

  @Autowired
  lateinit var motiveRepository: MotiveRepository

  @Autowired
  lateinit var securityLevelRepository: SecurityLevelRepository

  @Autowired
  lateinit var photoRepository: PhotoRepository

  @Autowired
  lateinit var azureBlobStorage: AzureBlobStorage

  fun initializeAzureBlobStorageContainers() {
    listOf("alle", "fggjeng", "husfolk").forEach {
      val containerClient = azureBlobStorage.blobServiceClient.createBlobContainer(it)
      containerClient.setAccessPolicy(PublicAccessType.BLOB, null)
    }
  }

  fun generateSecurityLevelData(): List<SecurityLevelDto> =
    listOf(
      SecurityLevelDto(
        securityLevelId = SecurityLevelId(UUID.fromString("8214142f-7c08-48ad-9130-fd7ac6b23e51")),
        securityLevelType = SecurityLevelType.FG
      ),
      SecurityLevelDto(
        securityLevelId = SecurityLevelId(UUID.fromString("8214142f-7c08-48ad-9130-fd7ac6b23e52")),
        securityLevelType = SecurityLevelType.HUSFOLK
      ),
      SecurityLevelDto(
        securityLevelId = SecurityLevelId(UUID.fromString("8314142f-7c08-48ad-9130-fd7ac6b23e52")),
        securityLevelType = SecurityLevelType.ALLE
      )
    )

  fun generateGangData(): List<GangDto> =
    listOf(
      GangDto(
        gangId = GangId(UUID.fromString("b0bd026f-cc19-4474-989c-aec8d4a76bc9")),
        name = "Fotogjengen"
      ),

      GangDto(
        gangId = GangId(UUID.fromString("b1bd026f-cc19-4474-989c-aec8d4a76bc9")),
        name = "Diversegjengen"
      )
    )

  fun getPhotoFromApi(): String {
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder()
      .uri(URI.create("https://picsum.photos/1200/800"))
      .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.headers().allValues("location")[0]
  }

  fun generatePhoto(): List<PhotoDto> {
    val list = mutableListOf<PhotoDto>()

    for (i in 1..50) {
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
          placeDto = generatePlaceData().first(),
          securityLevel = generateSecurityLevelData().first(),
          gang = generateGangData().first(),
          isGoodPicture = true,
          smallUrl = url,
          mediumUrl = url,
          photoGangBangerDto = generatePhotoGangBangerData().first(),
          photoTags = generatePhotoTagData(),
          albumDto = generateAlbumData().first(),
          categoryDto = generateCategoryData().first()
        )
      )
    }
    return list
  }

  fun generatePlaceData(): List<PlaceDto> =
    listOf(
      PlaceDto(
        placeId = PlaceId(UUID.fromString("9f4fa5d6-ad7c-419c-be58-1ee73f212675")),
        name = "Klubben"
      ),

      PlaceDto(
        placeId = PlaceId(UUID.fromString("8f4fa5d6-ad7c-419c-be58-1ee73f212675")),
        name = "Storsalen"
      )
    )

  fun generateMotiveData(): List<MotiveDto> =
    listOf(
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc4bcd")),
        title = "Amber Butts spiller på klubben",
        albumDto = generateAlbumData().first(),
        eventOwnerDto = generateEventOwnerData().first(),
        categoryDto = generateCategoryData().first(),
        dateCreated = LocalDate.now()
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc5bcd")),
        title = "High As a Kite 2020",
        albumDto = generateAlbumData().first(),
        eventOwnerDto = generateEventOwnerData().first(),
        categoryDto = generateCategoryData().first(),
        dateCreated = LocalDate.now()
      ),
    )
  fun generateCategoryData(): List<CategoryDto> =
    listOf(
      CategoryDto(
        categoryId = CategoryId(UUID.fromString("2832ee5e-3f11-4f11-8189-56ca4f70f418")),
        name = "Gjengfoto"
      ),

      CategoryDto(
        categoryId = CategoryId(UUID.fromString("3832ee5e-3f11-4f11-8189-56ca4f70f418")),
        name = "Konsert"
      )
    )

  fun generateEventOwnerData(): List<EventOwnerDto> =
    listOf(
      EventOwnerDto(
        eventOwnerId = EventOwnerId(UUID.fromString("9265f73d-7b13-4673-9f3b-1db3b6c7d526")),
        name = EventOwnerName.valueOf("Samfundet")
      ),
      EventOwnerDto(
        eventOwnerId = EventOwnerId(UUID.fromString("afc308c4-06e2-47bb-b97b-70eb3f55e8d9")),
        name = EventOwnerName.valueOf("ISFIT")
      ),
      EventOwnerDto(
        eventOwnerId = EventOwnerId(UUID.fromString("e91f1201-e0bf-4d25-8026-b2a2d44c37c3")),
        name = EventOwnerName.valueOf("UKA")
      )
    )

  fun generateAlbumData(): List<AlbumDto> {
    return listOf(
      AlbumDto(
        albumId = AlbumId(UUID.fromString("8a2bb663-1260-4c16-933c-a2af7420f5ff")),
        title = "Vår 2017"
      ),
      AlbumDto(
        albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f1")),
        title = "Høst 2017"
      ),
      AlbumDto(
        albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f2")),
        title = "Vår 2018"
      ),
      AlbumDto(
        albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f3")),
        title = "Høst 2018"
      ),
      AlbumDto(
        albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f4")),
        title = "Vår 2019"
      ),
      AlbumDto(
        albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f5")),
        title = "Høst 2019"
      )
    )
  }

  fun generatePhotoTagData(): List<PhotoTagDto> {
    return listOf(
      PhotoTagDto(
        photoTagId = PhotoTagId(UUID.fromString(("d8771ab3-28a9-4b8c-991d-01f6123b8590"))),
        name = "WowFactor100"
      ),
      PhotoTagDto(
        photoTagId = PhotoTagId(UUID.fromString(("d8771ab3-28a9-4b8c-991d-01f6123b8590"))),
        name = "insane!"
      ),
      PhotoTagDto(
        photoTagId = PhotoTagId(UUID.fromString(("d8771ab3-28a9-4b8c-991d-01f6123b8590"))),
        name = "Meh"
      )
    )
  }

  fun generateSamfundetUserData(): List<SamfundetUserDto> =
    listOf(
      SamfundetUserDto(
        samfundetUserId = SamfundetUserId(UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b822")),
        email = Email("emailtest@gmail.com"),
        firstName = "Sindre",
        lastName = "Sivertsen",
        profilePicturePath = "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        sex = "Mann",
        username = "sjsivert",
        securityLevel = generateSecurityLevelData().first(),
        phoneNumber = PhoneNumber("91382506")
      ),
      SamfundetUserDto(
        samfundetUserId = SamfundetUserId(UUID.fromString("7a89444f-25f6-44d9-8a73-94587d72b822")),
        email = Email("emailtest@gmail.com"),
        firstName = "Carolin",
        lastName = "Sandbråten",
        profilePicturePath = "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        sex = "Kvinne",
        username = "Carossa",
        securityLevel = generateSecurityLevelData().first(),
        phoneNumber = PhoneNumber("91382506")
      )
    )

  fun generatePhotoGangBangerData(): List<PhotoGangBangerDto> =
    listOf(
      PhotoGangBangerDto(
        photoGangBangerId = PhotoGangBangerId(UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b839")),
        address = "Odd Brochmanns Veg 52",
        city = "Trondheim",
        isActive = true,
        isPang = true,
        relationShipStatus = RelationshipStatus.valueOf("single"),
        zipCode = "7051",
        semesterStart = SemesterStart("H2018"),
        samfundetUser = generateSamfundetUserData().first(),
        position = generatePositionData().first()
      ),

      PhotoGangBangerDto(
        photoGangBangerId = PhotoGangBangerId(UUID.fromString("7a89444f-25f6-44d9-8a73-94587d72b839")),
        address = "Odd Brochmanns Veg 52",
        city = "Trondheim",
        isActive = true,
        isPang = true,
        relationShipStatus = RelationshipStatus.single,
        zipCode = "7051",
        semesterStart = SemesterStart("H2018"),
        samfundetUser = generateSamfundetUserData()[1],
        position = generatePositionData()[1]
      )
    )

  fun generatePhotoGangBangerPositionData(): List<PhotoGangBangerPositionDto> =
    listOf(
      PhotoGangBangerPositionDto(
        photoGangBangerPositionId = PhotoGangBangerPositionId(UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b831")),
        isCurrent = true,
        photoGangBangerDto = generatePhotoGangBangerData().first(),
        position = generatePositionData().first()
      ),
      PhotoGangBangerPositionDto(
        photoGangBangerPositionId = PhotoGangBangerPositionId(UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b832")),
        isCurrent = false,
        photoGangBangerDto = generatePhotoGangBangerData().first(),
        position = generatePositionData().first()
      )
    )

  fun generatePositionData(): List<PositionDto> =
    listOf(
      PositionDto(
        positionId = PositionId((UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f946"))),
        title = "Gjengsjef",
        email = Email("fg-web@samfundet.no")
      ),
      PositionDto(
        positionId = PositionId((UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f945"))),
        title = "Web",
        email = Email("fg-web@samfundet.no")
      )
    )

  fun seedMockData() {
    generateAlbumData()
      .forEach {
        albumRepository.create(it)
      }
    generatePhotoTagData().forEach {
      // hotoTagRepository.create(it)
    }
    println("PhotoTags Seeded")
    generateSecurityLevelData().forEach {
      securityLevelRepository.create(it)
    }
    generateSamfundetUserData().forEach {
      samfundetUserRepository.create(it)
    }
    println("SamunfdetUsers seeded")
    println(samfundetUserRepository.findAll().toString())
    generatePositionData().forEach {
      positionRepository.create(it)
    }
    generatePhotoTagData().forEach {
      // photoTagRepository.create(it)
    }
    println("Position seeded")
    println(positionRepository.findAll())

    generatePhotoGangBangerData().forEach {
      photoGangBangerRepository.create(it)
    }
    println("PhotoGangBangers seeded")
    generateCategoryData().forEach {
      categoryRepository.create(it)
    }
    println("Category seeded")

    generateEventOwnerData().forEach {
      eventOwnerRepository.create(it)
    }

    generateMotiveData().forEach {
      motiveRepository.create(it)
    }
    generatePlaceData().forEach {
      placeRepository.create(it)
    }
    generateGangData().forEach {
      gangRepository.create(it)
    }
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
    initializeAzureBlobStorageContainers()
    println("Photos Seeded")
  }
}
