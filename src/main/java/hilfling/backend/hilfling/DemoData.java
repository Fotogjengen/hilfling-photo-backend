package hilfling.backend.hilfling;

import hilfling.backend.hilfling.model.*;
import hilfling.backend.hilfling.repositories.*;
import hilfling.backend.hilfling.service.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@RestController
@RequestMapping("/seed")
public class DemoData {


    @Autowired
    private AlbumService albumService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EventOwnerService eventOwnerService;
    @Autowired
    private GangService gangService;
    @Autowired
    private MotiveService motiveService;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private PhotoGangBangerService photoGangBangerService;
    @Autowired
    private PhotoGangBangerPositionService photoGangBangerPositionService;
    @Autowired
    private PhotographyRequestService photographyRequestService;
    @Autowired
    private PhotoOnPurchaseOrderService photoOnPurchaseOrderService;
    @Autowired
    private PhotoTagService photoTagService;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private SecurityLevelService securityLevelService;

    @GetMapping
    public ResponseEntity<String> seedDemoData() {
        demoCategoryData(categoryService);
        demoAlbumData(albumService);
        demoSecurityLevelData(securityLevelService);
        demoPhotoGangBangerData(photoGangBangerService);
        demoArticleData(
                articleService,
                securityLevelService.getRepository(),
                photoGangBangerService.getRepository()
        );
        demoArticleTagData(articleTagService);
        demoEventOwnerData(eventOwnerService);
        demoGangData(gangService);
        demoMotiveData(
                motiveService,
                categoryService.getRepository(),
                eventOwnerService.getRepository(),
                albumService.getRepository()
        );
        demoPlaceData(placeService);
        demoPhotoData(
                photoService,
                motiveService.getRepository(),
                placeService.getRepository(),
                securityLevelService.getRepository(),
                gangService.getRepository(),
                photoGangBangerService.getRepository()
        );
        demoPositionData(positionService);
        demoPhotoGangBangerPositionData(
                photoGangBangerPositionService,
                photoGangBangerService.getRepository(),
                positionService.getRepository()
        );
        demoPhotographyRequestData(photographyRequestService);
        demoPurchaseOrderData(purchaseOrderService);
        demoPhotoOnPurchaseOrderData(
                photoOnPurchaseOrderService,
                photoService.getRepository(),
                purchaseOrderService.getRepository()
        );
        demoPhotoTagData(photoTagService);
        return ResponseEntity.ok("Data seeded");
    }

    public static void demoCategoryData(CategoryService service) {
        service.create(new Category("Fotostand"));
        service.create(new Category("Miljøbilde"));
        service.create(new Category("Interiør"));
        service.create(new Category("Exteriør"));
        service.create(new Category("Teater"));
        service.create(new Category("Studio"));
        service.create(new Category("Andre møter"));
        service.create(new Category("Portrett"));
        service.create(new Category("Kunst"));
        service.create(new Category("Studiobilde"));
        service.create(new Category("Konsert"));
    }

    public static void demoAlbumData(AlbumService service) {
        service.create(new Album("Vår 2017"));
        service.create(new Album("Høst 2017"));
        service.create(new Album("Vår 2018"));
        service.create(new Album("Høst 2018"));
        service.create(new Album("Vår 2019"));
        service.create(new Album("Høst 2019"));
        service.create(new Album("Høst 2019", true));
        service.create(new Album("Vår 2020"));
        service.create(new Album("Vår 2020", true));
    }

    public static void demoSecurityLevelData(SecurityLevelService service) {
        service.create(new SecurityLevel("FG"));
        service.create(new SecurityLevel("HUSFOLK"));
        service.create(new SecurityLevel("ALLE"));
    }

    public static void demoPhotoGangBangerData(PhotoGangBangerService service) {
        service.create(new PhotoGangBanger(
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
        ));

        service.create(new PhotoGangBanger(
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
        ));
    }

    public static void demoArticleData(
            ArticleService service,
            SecurityLevelRepository securityLevelRepository,
            PhotoGangBangerRepository photoGangBangerRepository
    ) {
        service.create(new Article(
                "Hvorfor bør lære deg vim",
                "Det er kult",
                securityLevelRepository.getOne(1L),
                photoGangBangerRepository.getOne(1L)
        ));
        service.create(new Article(
                "VSCode eller Webstorm?",
                "Webstorm",
                securityLevelRepository.getOne(2L),
                photoGangBangerRepository.getOne(2L)
        ));
        service.create(new Article(
                "Alle kan lese denne",
                "Det er megafett",
                securityLevelRepository.getOne(3L),
                photoGangBangerRepository.getOne(1L)
        ));
    }

    public static void demoArticleTagData(ArticleTagService service) {
        service.create(new ArticleTag("Morsom"));
        service.create(new ArticleTag("Fotografi"));
        service.create(new ArticleTag("Web"));
    }

    public static void demoEventOwnerData(EventOwnerService service) {
        service.create(new EventOwner("Samfundet"));
        service.create(new EventOwner("UKA"));
        service.create(new EventOwner("ISFiT"));
    }

    public static void demoGangData(GangService service) {
        service.create(new Gang("FG"));
        service.create(new Gang("KSG"));
        service.create(new Gang("KU"));
        service.create(new Gang("ITK"));
        service.create(new Gang("ARK"));
    }

    public static void demoMotiveData(
            MotiveService service,
            CategoryRepository categoryRepository,
            EventOwnerRepository eventOwnerRepository,
            AlbumRepository albumRepository
    ) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            service.create(new Motive(
                    "Bilder av Caroline",
                    formatter.parse("20/01/2020"),
                    categoryRepository.findByTitle("Portrett"),
                    eventOwnerRepository.getOne(1L),
                    albumRepository.getOne(1L)
            ));

            service.create(new Motive(
                    "Kakkamaddafakka",
                    formatter.parse("01/02/2020"),
                    categoryRepository.findByTitle("Konsert"),
                    eventOwnerRepository.getOne(2L),
                    albumRepository.getOne(3L)
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void demoPlaceData(PlaceService service) {
        service.create(new Place("Edgar"));
        service.create(new Place("Knaus"));
        service.create(new Place("Bodegaen"));
        service.create(new Place("Utenfor huset"));
        service.create(new Place("Daglighallen"));
        service.create(new Place("Studio"));
        service.create(new Place("Lyche"));
    }

    public static void demoPhotoData(
            PhotoService service,
            MotiveRepository motiveRepository,
            PlaceRepository placeRepository,
            SecurityLevelRepository securityLevelRepository,
            GangRepository gangRepository,
            PhotoGangBangerRepository photoGangBangerRepository
    ) {
        Photo photo = new Photo(
                true,
                motiveRepository.getOne(1L),
                placeRepository.getOne(1L),
                securityLevelRepository.getOne(1L),
                gangRepository.getOne(1L),
                photoGangBangerRepository.getOne(1L)
        );
        photo.setLargeUrl("https://foto.samfundet.no/media/alle/web/DIGF%C3%98/digf%C3%B81791.jpg");
        photo.setMediumUrl("https://foto.samfundet.no/media/alle/web/DIGF%C3%98/digf%C3%B81791.jpg");
        photo.setSmallUrl("https://foto.samfundet.no/media/alle/web/DIGF%C3%98/digf%C3%B81791.jpg");
        service.create(photo);
    }

    public static void demoPositionData(PositionService service) {
        service.create(new Position("Gjengsjef", "fg-sjef@samfundet.no"));
        service.create(new Position("Webutvikler", "fg-web@samfundet.no"));
        service.create(new Position("Koordineringssjef", "fg-koordinator@samfundet.no"));
    }

    public static void demoPhotoGangBangerPositionData(
            PhotoGangBangerPositionService service,
            PhotoGangBangerRepository photoGangBangerRepository,
            PositionRepository positionRepository
    ) {
        service.create(new PhotoGangBangerPosition(
                photoGangBangerRepository.getOne(1L),
                positionRepository.getOne(2L),
                false
        ));
        service.create(new PhotoGangBangerPosition(
                photoGangBangerRepository.getOne(1L),
                positionRepository.getOne(1L),
                true
        ));
    }

    public static void demoPhotographyRequestData(PhotographyRequestService service) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        try {
            service.create(new PhotographyRequest(
                    formatter.parse("5/02/2020 19:30"),
                    formatter.parse("5/02/2020 22:30"),
                    "Utenfor hovedbygget",
                    true,
                    "Gruppebilde",
                    "Herman Hermansen",
                    "herman@hermansen.com",
                    "12345678",
                    "Linjeforeningen skal ha bilde sammen."
            ));
            service.create(new PhotographyRequest(
                    formatter.parse("23/02/2020 16:30"),
                    formatter.parse("23/02/2020 22:30"),
                    "Rundhallen",
                    true,
                    "Fotostand",
                    "Gris grisemann",
                    "gris@samfundet.no",
                    "123456789",
                    "Fotostandforespørsel fra løk!!!"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void demoPurchaseOrderData(PurchaseOrderService service) {
        service.create(new PurchaseOrder(
                "Caroline Sandsbråten",
                "carosa@samfundet.no",
                "Magnus den Godes gt. 10",
                "7030",
                "Trondheim",
                true,
                "Trenger bildene ASAP",
                new Date(),
                false
        ));
        service.create(new PurchaseOrder(
                "Caroline Sandsbråten",
                "carosa@samfundet.no",
                "Magnus den Godes gt. 10",
                "7030",
                "Trondheim",
                false,
                "Trenger bildene ASAP",
                new Date(),
                true
        ));
    }

    public static void demoPhotoOnPurchaseOrderData(
            PhotoOnPurchaseOrderService service,
            PhotoRepository photoRepository,
            PurchaseOrderRepository purchaseOrderRepository
    ) {
        service.create(new PhotoOnPurchaseOrder(
                purchaseOrderRepository.getOne(1L),
                photoRepository.getOne(1L),
                "A4",
                2
        ));
        service.create(new PhotoOnPurchaseOrder(
                purchaseOrderRepository.getOne(2L),
                photoRepository.getOne(1L),
                "A1",
                1
        ));
    }

    public static void demoPhotoTagData(PhotoTagService service) {
        service.create(new PhotoTag("Hottie!!"));
        service.create(new PhotoTag("Grusomt bra bilde"));
        service.create(new PhotoTag("God Damn!"));
    }
}