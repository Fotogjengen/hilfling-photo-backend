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
            PhotoTagService photoTagService
    ) {
        return args -> {
            DemoData.demoCategoryData(categoryService);
            DemoData.demoAlbumData(albumService);
            DemoData.demoSecurityLevelData(securityLevelService);
            DemoData.demoPhotoGangBangerData(photoGangBangerService);
            DemoData.demoArticleData(
                    articleService,
                    securityLevelService.getRepository(),
                    photoGangBangerService.getRepository()
            );
            DemoData.demoArticleTagData(articleTagService);
            DemoData.demoEventOwnerData(eventOwnerService);
            DemoData.demoGangData(gangService);
            DemoData.demoMotiveData(
                    motiveService,
                    categoryService.getRepository(),
                    eventOwnerService.getRepository()
            );
            DemoData.demoPlaceData(placeService);
            DemoData.demoPhotoData(
                    photoService,
                    motiveService.getRepository(),
                    placeService.getRepository(),
                    securityLevelService.getRepository(),
                    gangService.getRepository(),
                    photoGangBangerService.getRepository()
            );
            DemoData.demoPositionData(positionService);
            DemoData.demoPhotoGangBangerPositionData(
                    photoGangBangerPositionService,
                    photoGangBangerService.getRepository(),
                    positionService.getRepository()
            );
            DemoData.demoPhotographyRequestData(photographyRequestService);
            DemoData.demoPurchaseOrderData(purchaseOrderService);
            DemoData.demoPhotoOnPurchaseOrderData(
                    photoOnPurchaseOrderService,
                    photoService.getRepository(),
                    purchaseOrderService.getRepository()
			);
            DemoData.demoPhotoTagData(photoTagService);
        };
    }
}
