package hilfling.backend.hilfling

/*
import no.fg.hilflingbackend.controller.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.*
import org.springframework.beans.factory.annotation.Autowired

class MockData2 {

    @Autowired
    val photoRepository = PhotoRepository()

    @Autowired
    val photoGangBangerRepository = PhotoGangBangerRepository()

    @Autowired
    val photographyRequestRepository = PhotographyRequestRepository()

    @Autowired
    val photoOnPurchaseOrderRepository = PhotoOnPurchaseOrderRepository()

    @Autowired
    val photoTagRepository = PhotoTagRepository()

    @Autowired
    val placeRepository = PlaceRepository()

    @Autowired
    val positionRepository = PositionRepository()

    @Autowired
    val purchaseOrderRepository = PurchaseOrderRepository()

    @Autowired
    val securitylevelRepository = SecurityLevelRepository()



    private fun seedAlbumData() {
        listOf(
                Album{
                    title = "Vår 2017";
                    isAnalog = true;
                },
                Album{
                    title = "Høst 2017";
                },
                Album {
                    title = "Vår 2018"
                },
                Album {
                    title = "Høst 2018"
                },
                Album {
                    title = "Vår 2019"
                },
                Album {
                    title = "Høst 2019"
                    isAnalog = true
                }
        ).forEach{
            albumRepository.create(it)
        }

    }

    private fun seedPhotoTagData() {
        listOf(
                PhotoTag{
                    title="WowFactor100"
                },
                PhotoTag{
                    title="insane!"
                }
                PhotoTag {
                    title = "Meh"
                }
        ).forEach{
            photoTagRepository.create(it)
        }

    }

    private fun seedPhotoOnPurchaseOrderData() {
        listOf(
                PhotoOnPurchaseOrder{
                    purchaseOrderRepository = purchaseOrderRepository.findById(1)
                    photoRepository = photoRepository.findById(2)
                    size = "A4"
                },
                PhotoOnPurchaseOrderData() {
                    purchaseOrderRepository = purchaseOrderRepository.findById(2)
                    photoRepository = photoRepository.findById(2)
                    size = "A3"
                }
        ).forEach{
            photoOnPurchaseOrderRepository.create(it)
        }

    }

    private fun seedPurchaseOrderData() {
        listOf(
                PurchaseOrder {
                    name = "Caroline Sandsbråten"
                    email = "carosa@samfundet.no"
                    address = "Magnus den Godes gt. 10"
                    zip-code = "7030"
                    city = "Trondheim"
                    sendByPost = true
                    comment = "TRENGER BILDENE ASAP!!!!"
                    isCompleted = false
                },
                PurchaseOrder {
                    name = "Oscar Selnes Bognæs"
                    email = "Oscarsb1@gmail.com"
                    address = "Frode Rinnas Vei 159"
                    zip-code = "7050"
                    city = "Trondheim"
                    sendByPost = true
                    comment = "Hvis jeg ikke får bildene innen fredag refunder jeg og legger igjen 1 stjerne på TripAdvisor"
                    isCompleted = false
                }
        ).forEach{
            purchaseOrderRepository.create(it)

        }

    }

    private fun seedPhotographyRequestData() {
        listOf(
                PhotographyRequest{
                    var dateInString = "2020-05-02"
                    var simpleFormat =  DateTimeFormatter.ISO_DATE;
                    var convertedDate = LocalDate.parse(dateInString, simpleFormat)
                    dateCreated = convertedDate
                    place = "Utenfor hovedbygget"
                    isGoodPicture = true
                    motive = "Gruppebilde"
                    securitylevel = 1
                    photoGangBanger = photoGangBangerRepository.findById(1)
                },
                PhotographyRequest{
                    var dateInString = "2019-12-11"
                    var simpleFormat =  DateTimeFormatter.ISO_DATE;
                    var convertedDate = LocalDate.parse(dateInString, simpleFormat)
                    dateCreated = convertedDate
                    place = "Realfagskjelleren"
                    isGoodPicture = true
                    motive = "Fæst"
                    securitylevel = 2
                    photoGangBanger = photoGangBangerRepository.findById(2)
                }
        ).forEach{
            photographyRequestRepository.create(it)
        }
    }

    private fun seedPositionData() {
        listOf(
                Position {
                    title = "Gjengsjef"
                },
                position {
                    title = "Økonomi"
                }
        ).forEach{
            positionRepository.create(it)
        }
    }

    private fun seedPhotoGangBangerPositionData() {
        listOf(
            PhotoGangBangerPositionData{
                photoGangBanger = PhotoGangBangerRepository.findById(1)
                position = PositionRepository.findById(1)
            },
            PhotoGangBangerPositionData{
                photoGangBanger = PhotoGangBangerRepository.findById(2)
                position = PositionRepository.findById(2)
            }
        ).forEach{
            PhotoGangBangerRepository.create(it)
        }
    }

    private fun seedPlaceData() {
        listOf(
                PlaceData{
                    dateCreated = "2019-05-11"
                    title = "Edgar"
                }
                PlaceData{
                    dateCreated = "2018-04-12"
                    title = "Lyche"
                }
                PlaceData{
                    dateCreated = "2019-11-12"
                    title = "Daglighallen"
                }
        ).forEach{
            PlaceRepository.create(it)
        }

    }

    private fun seedMotiveData() {
        listOf(
                MotiveData{
                    title = "Bilder av Caroline"
                    dateCreated = "2020-09-29"
                    category = CategoryRepository.findById(1)
                    event_owner = EventOwner.findById(1)
                    album_id = AlbumRepository.findById(1)
                }
                MotiveData{
                    title = "Kjekke bilder av Oscar"
                    dateCreated = "1865-09-29"
                    category = CategoryRepository.findById(1)
                    event_owner = EventOwner.findById(1)
                    album_id = AlbumRepository.findById(1)
                }
        ).forEach{
            MotiveRepository.create(it)
        }
    }

    private fun seedGangData() {
        listOf(
                GangData{
                    name = "Fotogjengen"
                    dateCreated = "1907-01-01"
                }
                GangData{
                    name = "Diversegjengen"
                    dateCreated = "1937-01-01"
                }
        ).forEach{
            GangRepository.create(it)
        }
    }

    private fun seedEventOwnerData(){
        ListOf(
                EventOwner {
                    name = "Samfundet"
                }
                EventOwner {
                    name "Uka"
                }
        )
    }

    private fun seedArticleTagData() {
        ListOf(
                ArticleTag {
                    dateCreated = "2020-02-02"
                    name = "Morsom"
                }
                ArticleTag {
                    dateCreated = "2019-04-08"
                    name = "Fotografi"
                }
                ArticleTag {
                    dateCreated = "2018-12-01"
                    name = "Web"
                }
        )
    }



    //To be finished (Making motive before)
    private fun seedPhotoData() {
        listOf(
                Photo {
                    dateCreated = "2020-01-01"
                    s_url = "https://www.pngitem.com/pimgs/m/141-1412402_transparent-pepe-clipart-hd-png-download.png"
                    m_url = "https://www.pngitem.com/pimgs/m/141-1412402_transparent-pepe-clipart-hd-png-download.png"
                    l_url = "https://www.pngitem.com/pimgs/m/141-1412402_transparent-pepe-clipart-hd-png-download.png"

                }

        )
    }

    private fun seedMotiveData(){

    }

    fun seedMockData () {
        seedPhotoTagData()
        seedAlbumData()
        seedPhotoOnPurchaseOrderData()
        seedPhotographyRequestData()
        seedPositionData()
        seedPhotoGangBangerPositionData()
    }


}
*/
