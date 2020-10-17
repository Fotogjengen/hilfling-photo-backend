/*
package no.fg.hilflingbackend

import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.model.*
import no.fg.hilflingbackend.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MockDataService {
  @Autowired
  lateinit var albumRepository: AlbumRepository

  @Autowired
  lateinit var articleRepository: ArticleRepository

  @Autowired
  lateinit var photoTagRepository: PhotoTagRepository

  @Autowired
  lateinit var articleTagRepository: ArticleTagRepository

  @Autowired
  lateinit var category: CategoryRepository

  @Autowired
  lateinit var eventOwnerRepository: EventOwnerRepository

  @Autowired
  lateinit var gangRepository: GangRepository

  @Autowired
  lateinit var motiveRepository: MotiveRepository

  private fun generateAlbumData(): List<AlbumDto> {
    listOf(
      AlbumDto(
        title = "Vår 2017",
        isAnalog = true,
      ),
      AlbumDto(
        title = "Høst 2017"
    ),
    AlbumDto(
      title = "Vår 2018"
    ),
    AlbumDto(
      title = "Høst 2018"
    ),
    AlbumDto(
      title = "Vår 2019"
    ),
    AlbumDto(
      title = "Høst 2019",
      isAnalog = true
    )

  }
  private fun generatePhotoTagData(): List<PhotoTag> {
    return listOf(
      PhotoTag {
        name = "WowFactor100"
      },
      PhotoTag {
        name = "insane!"
      },
      PhotoTag {
        name = "Meh"
      }
    )
  }

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

  private fun generatePositionData(): List<Position> {
    return listOf(
      Position {
        title = "Gjengsjef"
        email = "fg-web@samfundet.no"
      },
      Position {
        title = "Web"
        email = "fg-web@samfundet.no"
      }
    )
  }

  private fun generatePhotoGangBangerPositionData(): List<PhotoGangBangerPosition> {
    return listOf(
      PhotoGangBangerPosition {
        isCurrent = true
        photoGangBanger = gener
        position = PositionRepository.findById(1)
      },
      PhotoGangBangerPosition {
        photoGangBanger = PhotoGangBangerRepository.findById(2)
        position = PositionRepository.findById(2)
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

  private fun generateMotiveData(): List<Motive> {
    return listOf(
      Motive {
        title = "Bilder av Caroline"
        dateCreated = LocalDate.parse("2020-09-29")
        category = CategoryRepository.findById(1)
        event_owner = EventOwner.findById(1)
        album_id = AlbumRepository.findById(1)
      }
        MotiveData {
        title = "Kjekke bilder av Oscar"
        dateCreated = "1865-09-29"
        category = CategoryRepository.findById(1)
        event_owner = EventOwner.findById(1)
        album_id = AlbumRepository.findById(1)
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

  private fun generateMotiveData() {

  }

  fun seedMockData() {
    generateAlbumData()
  }


}
*/
