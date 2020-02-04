package hilfling.backend.hilfing;

import hilfling.backend.hilfing.model.*;
import hilfling.backend.hilfing.repositories.*;
import hilfling.backend.hilfing.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class HilfingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HilfingApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoPhotoTagData(
            AlbumService albumService,
            ArticleService articleService,
            ArticleTagService articleTagService,
            CategoryService categoryService,
            EventOwnerService eventOwnerService,
            GangService gangService,
            MotiveService motiveService,
            PhotoGangBangerPositionService photoGangBangerPositionService,
            PhotoGangBangerService photoGangBangerService,
            PhotographyRequestService photographyRequestService,
            PhotoOnPurchaseOrderService photoOnPurchaseOrderService,
            PhotoService photoService,
            PositionService positionService,
            PurchaseOrderService purchaseOrderService,
            SecurityLevelService securityLevelService,
            PlaceService placeService,
            PhotoTagService photoTagService,

            SecurityLevelRepository securityLevelRepository,
            PhotoGangBangerRepository photoGangBangerRepository,
            CategoryRepository categoryRepository,
            EventOwnerRepository eventOwnerRepository,
            MotiveRepository motiveRepository,
            PlaceRepository placeRepository,
            GangRepository gangRepository,
            PositionRepository positionRepository,
            PhotoRepository photoRepository,
            PurchaseOrderRepository purchaseOrderRepository
    ) {
        return args -> {
            DemoData.demoCategoryData(categoryService);
            DemoData.demoAlbumData(albumService);
            DemoData.demoSecurityLevelData(securityLevelService);
            DemoData.demoPhotoGangBangerData(photoGangBangerService);
            DemoData.demoArticleData(articleService, securityLevelRepository, photoGangBangerRepository);
            DemoData.demoArticleTagData(articleTagService);
            DemoData.demoEventOwnerData(eventOwnerService);
            DemoData.demoGangData(gangService);
            DemoData.demoMotiveData(motiveService, categoryRepository, eventOwnerRepository);
            DemoData.demoPlaceData(placeService);
            DemoData.demoPhotoData(
                    photoService,
                    motiveRepository,
                    placeRepository,
                    securityLevelRepository,
                    gangRepository,
                    photoGangBangerRepository
            );
            DemoData.demoPositionData(positionService);
            DemoData.demoPhotoGangBangerPositionData(
                    photoGangBangerPositionService,
                    photoGangBangerRepository,
                    positionRepository
            );
            DemoData.demoPhotographyRequestData(photographyRequestService);
            DemoData.demoPurchaseOrderData(purchaseOrderService);
            DemoData.demoPhotoOnPurchaseOrderData(
                    photoOnPurchaseOrderService,
                    photoRepository,
                    purchaseOrderRepository
			);
            DemoData.demoPhotoTagData(photoTagService);
        };
    }
}
