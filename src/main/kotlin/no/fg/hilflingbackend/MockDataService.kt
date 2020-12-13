package no.fg.hilflingbackend

import me.liuwj.ktorm.database.Database
import no.fg.hilflingbackend.dto.*
import no.fg.hilflingbackend.model.*
import no.fg.hilflingbackend.repository.*
import no.fg.hilflingbackend.value_object.Email
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class MockDataService {
  @Autowired
  lateinit var database: Database

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
  lateinit var articleRepository: ArticleRepository

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

  private fun generateSecurityLevelData(): List<SecurityLevel> =
    listOf(
      SecurityLevel {
        id = UUID.fromString("8214142f-7c08-48ad-9130-fd7ac6b23e51")
        type = "FG"
      },
      SecurityLevel {
        id = UUID.fromString("8214142f-7c08-48ad-9130-fd7ac6b23e52")
        type = "HUSFOLK"
      }
    )

  private fun generateGangData(): List<GangDto> =
    listOf(
      GangDto(
        gangId = GangId(UUID.fromString("b0bd026f-cc19-4474-989c-aec8d4a76bc9")),
        name = "Fotogjengen"
      )
    )

  private fun generatePhoto(): List<PhotoDto> =
    listOf(
      PhotoDto(
        photoId = PhotoId(UUID.fromString("8214142f-7c08-48ad-9130-fd7ac6b23e58")),
        largeUrl = "https://i.redd.it/f00ixlwhmud21.png",
        motive = generateMotiveData().first(),
        place = generatePlaceData().first().toEntity(),
        // TODO: generateSecutyry level and finish generatePhoto
        securityLevel = generateSecurityLevelData().first(),
        gang = generateGangData().first().toEntity(),
        isGoodPicture = true,
        photoGangBanger = generatePhotoGangBangerData().first().toEntity()
      )
    )
  private fun generatePlaceData(): List<PlaceDto> =
    listOf(
      PlaceDto(
        placeId = PlaceId(UUID.fromString("9f4fa5d6-ad7c-419c-be58-1ee73f212675")),
        name = "Klubben"
      )
    )

  private fun generateMotiveData(): List<Motive> =
    listOf(
      Motive {
        id = UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc4bcd")
        title = "Amber Butts spiller på klubben"
        album = generateAlbumData().first().toEntity()
        eventOwner = generateEventOwnerData().first()
        category = generateCategoryData().first().toEntity()
      }
    )
  private fun generateCategoryData(): List<CategoryDto> =
    listOf(
      CategoryDto(
        categoryId = CategoryId(UUID.fromString("2832ee5e-3f11-4f11-8189-56ca4f70f418")),
        name = "Gjengfoto"
      )
    )

  private fun generateEventOwnerData(): List<EventOwner> =
    listOf(
      EventOwner {
        id = UUID.fromString("afc308c4-06e2-47bb-b97b-70eb3f55e8d9")
        name = "ISFIT"
      },
      EventOwner {
        id = UUID.fromString("9265f73d-7b13-4673-9f3b-1db3b6c7d526")
        name = "Samfundet"
      },
      EventOwner {
        id = UUID.fromString("e91f1201-e0bf-4d25-8026-b2a2d44c37c3")
        name = "UKA"
      }
    )

  private fun generateAlbumData(): List<AlbumDto> {
    return listOf(
      AlbumDto(
        albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f8")),
        title = "Vår 2017",
        isAnalog = true,
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
        title = "Høst 2019",
        isAnalog = true
      )
    )
  }

  private fun generatePhotoTagData(): List<PhotoTagDto> {
    return listOf(
      PhotoTagDto(
        name = "WowFactor100"
      ),
      PhotoTagDto(
        name = "insane!"
      ),
      PhotoTagDto(
        name = "Meh"
      )
    )
  }

  private fun generateSamfundetUserData(): List<SamfundetUserDto> =
    listOf(
      SamfundetUserDto(
        samfundetUserId = SamfundetUserId(UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b822")),
        email = Email.create("emailtest@gmail.com"),
        firstName = "Sindre",
        lastName = "Sivertsen",
        profilePicturePath = "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        sex = "Mann",
        username = "sjsivert",
        securituLevel = SecurityLevel {
          type = "1"
        },
        phoneNumber = "91382506"
      )
    )

  private fun generatePhotoGangBangerData(): List<PhotoGangBangerDto> =
    listOf(
      PhotoGangBangerDto(
        photoGangBangerId = PhotoGangBangerId(UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b839")),
        address = "Odd Brochmanns Veg 52",
        city = "Trondheim",
        isActive = true,
        isPang = true,
        relationShipStatus = RelationshipStatus.SINGLE,
        zipCode = "7051",
        semesterStart = SemesterStart("H2018"),
        samfundetUser = generateSamfundetUserData().first(),
        position = generatePositionData().first()
      )
    )

  private fun generatePhotoGangBangerPositionData(): List<PhotoGangBangerPositionDto> =
    listOf(
      PhotoGangBangerPositionDto(
        photoGangBangerPositionId = PhotoGangBangerPositionId(UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b831")),
        isCurrent = true,
        photoGangBangerDto = generatePhotoGangBangerData().first(),
        position = generatePositionData().first(),
      ),
      PhotoGangBangerPositionDto(
        photoGangBangerPositionId = PhotoGangBangerPositionId(UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b832")),
        isCurrent = false,
        photoGangBangerDto = generatePhotoGangBangerData().first(),
        position = generatePositionData().first(),
      )
    )

  private fun generatePositionData(): List<PositionDto> =
    listOf(
      PositionDto(
        positionId = PositionId((UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f946"))),
        title = "Gjengsjef",
        email = Email.create("fg-web@samfundet.no")
      ),
      PositionDto(
        positionId = PositionId((UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f945"))),
        title = "Web",
        email = Email.create("fg-web@samfundet.no")
      )
    )
  /*
  private fun generatePhotoOnPurchaseOrderData(): List<PhotoOnPurchaseOrder> {
    return listOf(
      PhotoOnPurchaseOrder {
        photo = generatePhotoData().single {
          it.id == 1
        }
        purchaseOrder = generatePurchaseOrderData().single {
          it.id == 1
        }
        amount = 2
        size = "A4"

      },
      PhotoOnPurchaseOrder {
        photo = generatePhotoData().single {
          it.id == 2
        }
        purchaseOrder = generatePurchaseOrderData().single {
          it.id == 1
        }
        amount = 2
        size = "A4"
      }
    )
  }

  private fun generatePurchaseOrderData(): List<PurchaseOrder> {
    return listOf(
      PurchaseOrder {
        name = "Caroline Sandsbråten"
        email = "carosa@samfundet.no"
        address = "Magnus den Godes gt. 10"
        zipCode = "7030"
        city = "Trondheim"
        sendByPost = true
        comment = "TRENGER BILDENE ASAP!!!!"
        isCompleted = false
      },
      PurchaseOrder {
        name = "Oscar Selnes Bognæs"
        email = "Oscarsb1@gmail.com"
        address = "Frode Rinnas Vei 159"
        zipCode = "7051"
        city = "Trondheim"
        sendByPost = true
        comment = "Hvis jeg ikke får bildene innen fredag refunder jeg og legger igjen 1 stjerne på TripAdvisor"
        isCompleted = false
      }
    )
  }

  private fun generatePhotographyRequestData(): List<PhotographyRequest> {
    return listOf(
      PhotographyRequest {
        description = "Can u take photos uf us pls"
        email = "Sindreerkul@kulesindre.kul"
        isIntern = true
        startTime = LocalDate.parse("2019-12-11")
        endTime = LocalDate.parse("2019-12-11")
        place = "Your mamas house"
        phone = "91382405"
        type = "Gruppebilde"
      },
      PhotographyRequest {
        description = "Kan dere ta bilder av oss mens vi skjærer ost?"
        email = "email@email.no"
        isIntern = false
        startTime = LocalDate.parse("2019-12-11")
        endTime = LocalDate.parse("2019-12-11")
        place = "Odd Brochmanss Veg 62"
        phone = "91382405"
        type = "Stilbilde"
      }
    )
  }



  private fun generatePlaceData(): List<Place> {
    return listOf(
      PlaceData {
        dateCreated = "2019-05-11"
        title = "Edgar"
      }
        PlaceData {
        dateCreated = "2018-04-12"
        title = "Lyche"
      }
        PlaceData {
        dateCreated = "2019-11-12"
        title = "Daglighallen"
      }
    )

  }


  private fun generateGangData(): List<Gang> {
    return listOf(
      Gang {
        name = "Fotogjengen"
        dateCreated = LocalDate.parse("1907-01-01")
      },
      Gang {
        name = "Diversegjengen"
        dateCreated = LocalDate.parse("1937-01-01")
      }
    )
  }

  private fun generateEventOwnerData() {
    return listOf(
      EventOwner {
        name = "Samfundet"
      },
      EventOwner {
        name = "Uka"
      }
    )
  }

  private fun generateArticleTagData(): List<ArticleTag> {
    return listOf(
      ArticleTag {
        dateCreated = LocalDate.parse("2020-02-02")
        name = "Morsom"
      },
      ArticleTag {
        dateCreated = LocalDate.parse("2019-04-08")
        name = "Fotografi"
      },
      ArticleTag {
        dateCreated = LocalDate.parse("2018-12-01")
        name = "Web"
      }
    )
  }


  //To be finished (Making motive before)
  private fun generatePhotoData(): List<Photo> {
    listOf(
      Photo {
        dateCreated = LocalDate.parse("2020-01-01")
        largeUrl = "https://www.pngitem.com/pimgs/m/141-1412402_transparent-pepe-clipart-hd-png-download.png"
        mediumUrl = "https://www.pngitem.com/pimgs/m/141-1412402_transparent-pepe-clipart-hd-png-download.png"
        smallUrl = "https://www.pngitem.com/pimgs/m/141-1412402_transparent-pepe-clipart-hd-png-download.png"


      }
    )
  }


*/

  fun seedMockData() {
    generateAlbumData()
      .forEach {
        albumRepository.create(it)
      }
    generatePhotoTagData().forEach {
      photoTagRepository.create(it)
    }
    generateSamfundetUserData().forEach {
      samfundetUserRepository.create(it)
    }
    println("SamunfdetUsers seeded")
    println(samfundetUserRepository.findAll().toString())
    generatePositionData().forEach {
      positionRepository.create(it)
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
    println("Eventowner seeded")

    generateMotiveData().forEach {
      motiveRepository.create(it)
    }
    generatePlaceData().forEach {
      placeRepository.create(it)
    }
    generateSecurityLevelData().forEach {
      securityLevelRepository.create(it)
    }
    generateGangData().forEach {
      gangRepository.create(it)
    }
    generatePhoto().forEach {
      photoRepository.createPhoto(it.toEntity())
    }
  }
}
