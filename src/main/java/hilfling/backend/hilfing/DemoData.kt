package hilfling.backend.hilfing

import hilfling.backend.hilfing.model.*
import hilfling.backend.hilfing.repositories.*
import hilfling.backend.hilfing.service.*
import lombok.Data
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Data
@RestController
@RequestMapping("/seed")
class DemoData {
    @Autowired
    private val albumService: AlbumService? = null

    @Autowired
    private val articleService: ArticleService? = null

    @Autowired
    private val articleTagService: ArticleTagService? = null

    @Autowired
    private val categoryService: CategoryService? = null

    @Autowired
    private val eventOwnerService: EventOwnerService? = null

    @Autowired
    private val gangService: GangService? = null

    @Autowired
    private val motiveService: MotiveService? = null

    @Autowired
    private val photoService: PhotoService? = null

    @Autowired
    private val photoGangBangerService: PhotoGangBangerService? = null

    @Autowired
    private val photoGangBangerPositionService: PhotoGangBangerPositionService? = null

    @Autowired
    private val photographyRequestService: PhotographyRequestService? = null

    @Autowired
    private val photoOnPurchaseOrderService: PhotoOnPurchaseOrderService? = null

    @Autowired
    private val photoTagService: PhotoTagService? = null

    @Autowired
    private val placeService: PlaceService? = null

    @Autowired
    private val positionService: PositionService? = null

    @Autowired
    private val purchaseOrderService: PurchaseOrderService? = null

    @Autowired
    private val securityLevelService: SecurityLevelService? = null

    @GetMapping
    fun seedDemoData(): ResponseEntity<String> {
        demoCategoryData(categoryService)
        demoAlbumData(albumService)
        demoSecurityLevelData(securityLevelService)
        demoPhotoGangBangerData(photoGangBangerService)
        demoArticleData(
                articleService,
                securityLevelService!!.repository,
                photoGangBangerService!!.repository
        )
        demoArticleTagData(articleTagService)
        demoEventOwnerData(eventOwnerService)
        demoGangData(gangService)
        demoMotiveData(
                motiveService,
                categoryService!!.repository,
                eventOwnerService!!.repository
        )
        demoPlaceData(placeService)
        demoPhotoData(
                photoService,
                motiveService!!.repository,
                placeService!!.repository,
                securityLevelService.repository,
                gangService!!.repository,
                photoGangBangerService.repository
        )
        demoPositionData(positionService)
        demoPhotoGangBangerPositionData(
                photoGangBangerPositionService,
                photoGangBangerService.repository,
                positionService!!.repository
        )
        demoPhotographyRequestData(photographyRequestService)
        demoPurchaseOrderData(purchaseOrderService)
        demoPhotoOnPurchaseOrderData(
                photoOnPurchaseOrderService,
                photoService!!.repository,
                purchaseOrderService!!.repository
        )
        demoPhotoTagData(photoTagService)
        return ResponseEntity.ok("Data seeded")
    }

    companion object {
        fun demoCategoryData(service: CategoryService?) {
            service!!.create(Category("Fotostand"))
            service.create(Category("Miljøbilde"))
            service.create(Category("Interiør"))
            service.create(Category("Exteriør"))
            service.create(Category("Teater"))
            service.create(Category("Studio"))
            service.create(Category("Andre møter"))
            service.create(Category("Portrett"))
            service.create(Category("Kunst"))
            service.create(Category("Studiobilde"))
            service.create(Category("Konsert"))
        }

        fun demoAlbumData(service: AlbumService?) {
            service!!.create(Album("Vår 2017"))
            service.create(Album("Høst 2017"))
            service.create(Album("Vår 2018"))
            service.create(Album("Høst 2018"))
            service.create(Album("Vår 2019"))
            service.create(Album("Høst 2019"))
            service.create(Album("Høst 2019", true))
            service.create(Album("Vår 2020"))
            service.create(Album("Vår 2020", true))
        }

        fun demoSecurityLevelData(service: SecurityLevelService?) {
            service!!.create(SecurityLevel("FG"))
            service.create(SecurityLevel("HUSFOLK"))
            service.create(SecurityLevel("ALLE"))
        }

        fun demoPhotoGangBangerData(service: PhotoGangBangerService?) {
            service!!.create(PhotoGangBanger(
                    "Caroline",
                    "Sandsbråten",
                    "carosa",
                    "carosa@samfundet.no",
                    "https://www.wwf.no/assets/article_images/Dyr/_656xAUTO_fit_center-center_none/Koala_WW2142346_ny.jpg",
                    "94812815",
                    "Kvinne",
                    "Singel",
                    "2017",
                    true,
                    true,
                    "Magnus den godes gate 10",
                    "7030",
                    "Trondheim"
            ))
            service.create(PhotoGangBanger(
                    "Sindre",
                    "Sivertsen",
                    "sindre",
                    "sindre@samfundet.no",
                    "https://www.germer.com/wp-content/uploads/2018/01/Germer-November-2017-0037-e1516900068181.jpg",
                    "55558888",
                    "Nørd",
                    "Singel",
                    "2018",
                    true,
                    true,
                    "Sindres hjem 2",
                    "7035",
                    "Trondheim"
            ))
        }

        fun demoArticleData(
                service: ArticleService?,
                securityLevelRepository: SecurityLevelRepository,
                photoGangBangerRepository: PhotoGangBangerRepository
        ) {
            service!!.create(Article(
                    "Hvorfor bør lære deg vim",
                    "Det er kult",
                    securityLevelRepository.getOne(1L),
                    photoGangBangerRepository.getOne(1L)
            ))
            service.create(Article(
                    "VSCode eller Webstorm?",
                    "Webstorm",
                    securityLevelRepository.getOne(2L),
                    photoGangBangerRepository.getOne(2L)
            ))
            service.create(Article(
                    "Alle kan lese denne",
                    "Det er megafett",
                    securityLevelRepository.getOne(3L),
                    photoGangBangerRepository.getOne(1L)
            ))
        }

        fun demoArticleTagData(service: ArticleTagService?) {
            service!!.create(ArticleTag("Morsom"))
            service.create(ArticleTag("Fotografi"))
            service.create(ArticleTag("Web"))
        }

        fun demoEventOwnerData(service: EventOwnerService?) {
            service!!.create(EventOwner("Samfundet"))
            service.create(EventOwner("UKA"))
            service.create(EventOwner("ISFiT"))
        }

        fun demoGangData(service: GangService?) {
            service!!.create(Gang("FG"))
            service.create(Gang("KSG"))
            service.create(Gang("KU"))
            service.create(Gang("ITK"))
            service.create(Gang("ARK"))
        }

        fun demoMotiveData(
                service: MotiveService?,
                categoryRepository: CategoryRepository,
                eventOwnerRepository: EventOwnerRepository
        ) {
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            try {
                service!!.create(Motive(
                        "Bilder av Caroline",
                        formatter.parse("20/01/2020"),
                        categoryRepository.findByTitle("Portrett"),
                        eventOwnerRepository.getOne(1L)
                ))
                service.create(Motive(
                        "Kakkamaddafakka",
                        formatter.parse("01/02/2020"),
                        categoryRepository.findByTitle("Konsert"),
                        eventOwnerRepository.getOne(2L)
                ))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        fun demoPlaceData(service: PlaceService?) {
            service!!.create(Place("Edgar"))
            service.create(Place("Knaus"))
            service.create(Place("Bodegaen"))
            service.create(Place("Utenfor huset"))
            service.create(Place("Daglighallen"))
            service.create(Place("Studio"))
            service.create(Place("Lyche"))
        }

        fun demoPhotoData(
                service: PhotoService?,
                motiveRepository: MotiveRepository,
                placeRepository: PlaceRepository,
                securityLevelRepository: SecurityLevelRepository,
                gangRepository: GangRepository,
                photoGangBangerRepository: PhotoGangBangerRepository
        ) {
            service!!.create(Photo(
                    "https://foto.samfundet.no/media/alle/web/DIGF%C3%98/digf%C3%B81791.jpg",
                    "https://foto.samfundet.no/media/alle/web/DIGF%C3%98/digf%C3%B81791.jpg",
                    "https://foto.samfundet.no/media/alle/web/DIGF%C3%98/digf%C3%B81791.jpg",
                    true,
                    motiveRepository.getOne(1L),
                    placeRepository.getOne(1L),
                    securityLevelRepository.getOne(1L),
                    gangRepository.getOne(1L),
                    photoGangBangerRepository.getOne(1L)
            ))
        }

        fun demoPositionData(service: PositionService?) {
            service!!.create(Position("Gjengsjef", "fg-sjef@samfundet.no"))
            service.create(Position("Webutvikler", "fg-web@samfundet.no"))
            service.create(Position("Koordineringssjef", "fg-koordinator@samfundet.no"))
        }

        fun demoPhotoGangBangerPositionData(
                service: PhotoGangBangerPositionService?,
                photoGangBangerRepository: PhotoGangBangerRepository,
                positionRepository: PositionRepository
        ) {
            service!!.create(PhotoGangBangerPosition(
                    photoGangBangerRepository.getOne(1L),
                    positionRepository.getOne(2L),
                    false
            ))
            service.create(PhotoGangBangerPosition(
                    photoGangBangerRepository.getOne(1L),
                    positionRepository.getOne(1L),
                    true
            ))
        }

        fun demoPhotographyRequestData(service: PhotographyRequestService?) {
            val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm")
            try {
                service!!.create(PhotographyRequest(
                        formatter.parse("5/02/2020 19:30"),
                        formatter.parse("5/02/2020 22:30"),
                        "Utenfor hovedbygget",
                        true,
                        "Gruppebilde",
                        "Herman Hermansen",
                        "herman@hermansen.com",
                        "12345678",
                        "Linjeforeningen skal ha bilde sammen."
                ))
                service.create(PhotographyRequest(
                        formatter.parse("23/02/2020 16:30"),
                        formatter.parse("23/02/2020 22:30"),
                        "Rundhallen",
                        true,
                        "Fotostand",
                        "Gris grisemann",
                        "gris@samfundet.no",
                        "123456789",
                        "Fotostandforespørsel fra løk!!!"
                ))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        fun demoPurchaseOrderData(service: PurchaseOrderService?) {
            service!!.create(PurchaseOrder(
                    "Caroline Sandsbråten",
                    "carosa@samfundet.no",
                    "Magnus den Godes gt. 10",
                    "7030",
                    "Trondheim",
                    true,
                    "Trenger bildene ASAP",
                    Date(),
                    false
            ))
            service.create(PurchaseOrder(
                    "Caroline Sandsbråten",
                    "carosa@samfundet.no",
                    "Magnus den Godes gt. 10",
                    "7030",
                    "Trondheim",
                    false,
                    "Trenger bildene ASAP",
                    Date(),
                    true
            ))
        }

        fun demoPhotoOnPurchaseOrderData(
                service: PhotoOnPurchaseOrderService?,
                photoRepository: PhotoRepository,
                purchaseOrderRepository: PurchaseOrderRepository
        ) {
            service!!.create(PhotoOnPurchaseOrder(
                    purchaseOrderRepository.getOne(1L),
                    photoRepository.getOne(1L),
                    "A4",
                    2
            ))
            service.create(PhotoOnPurchaseOrder(
                    purchaseOrderRepository.getOne(2L),
                    photoRepository.getOne(1L),
                    "A1",
                    1
            ))
        }

        fun demoPhotoTagData(service: PhotoTagService?) {
            service!!.create(PhotoTag("Hottie!!"))
            service.create(PhotoTag("Grusomt bra bilde"))
            service.create(PhotoTag("God Damn!"))
        }
    }
}