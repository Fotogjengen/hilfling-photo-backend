
import com.nhaarman.mockitokotlin2.mock
import no.fg.hilflingbackend.MockDataService
import no.fg.hilflingbackend.controller.PhotoController
import no.fg.hilflingbackend.service.PhotoService
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.springframework.core.io.ClassPathResource
import org.springframework.mock.web.MockMultipartFile
import java.security.InvalidParameterException
import java.util.UUID

class PhotoControllerSpec : Spek({
  val log = LoggerFactory.getLogger(this::class.java)

  describe("PhotoChontroller") {
    val mockDataService = MockDataService()
    val photoFile = ClassPathResource("demoPhotos/digfø3652.jpg")
    val mockPhotoService = mock<PhotoService> {
      on {
        saveDigitalPhotos(
          isGoodPictureList = listOf(true, true),
          motiveIdList = listOf(
            mockDataService.generateMotiveData()[0].id,
            mockDataService.generateMotiveData()[1].id
          ),
          placeIdList = listOf(
            mockDataService.generatePlaceData().first().placeId.id,
            mockDataService.generatePlaceData()[1].placeId.id
          ),
          securityLevelIdList = listOf(
            mockDataService.generateSecurityLevelData().first().securityLevelId.id,
            mockDataService.generateSecurityLevelData()[1].securityLevelId.id
          ),
          gangIdList = listOf(
            mockDataService.generateGangData()[0].gangId.id,
            mockDataService.generateGangData()[1].gangId.id
          ),
          photoGangBangerIdList = listOf(
            mockDataService.generatePhotoGangBangerData()[0].photoGangBangerId.id,
            mockDataService.generatePhotoGangBangerData()[1].photoGangBangerId.id
          ),
          fileList = listOf(
            MockMultipartFile("file", photoFile.filename, "text/plain", photoFile.inputStream),
            MockMultipartFile("file", photoFile.filename, "text/plain", photoFile.inputStream)
          )
        )
      }
    }
    val photoController = PhotoController(
      photoService = mockPhotoService
    )

    it("Fails when parameter list are unequal") {

      val notAPhotoFile = ClassPathResource("demoPhotos/not-an-image.exe")
        .file
      val multipartFile =
        MockMultipartFile("file", notAPhotoFile.name, "text/plain", notAPhotoFile.inputStream())
      assertThrows<InvalidParameterException> {
        photoController.uploadPhoto(
          isGoodPictureList = listOf(false, true),
          motiveIdList = listOf(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc4bcd")),
          placeIdList = listOf(UUID.fromString("9f4fa5d6-ad7c-419c-be58-1ee73f212675")),
          securityLevelIdList = listOf(UUID.fromString("8214142f-7c08-48ad-9130-fd7ac6b23e51")),
          gangIdList = listOf(UUID.fromString("b1bd026f-cc19-4474-989c-aec8d4a76bc9")),
          photoGangBangerIdList = listOf(UUID.fromString("7a89444f-25f6-44d9-8a73-94587d72b839")),
          fileList = listOf(multipartFile),
        )
      }
    }
  }
})
