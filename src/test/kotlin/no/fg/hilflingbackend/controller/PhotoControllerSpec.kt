import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import no.fg.hilflingbackend.MockDataService
import no.fg.hilflingbackend.controller.PhotoController
import no.fg.hilflingbackend.service.PhotoService
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.slf4j.LoggerFactory
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.security.InvalidParameterException
import java.util.UUID
import kotlin.test.assertEquals

class PhotoControllerSpec : Spek({
  val log = LoggerFactory.getLogger(this::class.java)

  describe("PhotoChontroller") {
    // Arrange
    val mockDataService = MockDataService()
    val photoFile = ClassPathResource("demoPhotos/digfø3652.jpg")
    val mockPhotoService = mock<PhotoService> {
      on { getAll() } doReturn mockDataService.generatePhoto()
    }
    val photoController = PhotoController(
      photoService = mockPhotoService
    )
    describe("GetAll()") {
      it("Can get all photos") {
        photoController.getAll() // Act
        Mockito.verify(mockPhotoService, atLeast(1)).getAll() // Assert
      }
    }
    describe("UploadPhoto()") {
      val notAPhotoFile = ClassPathResource("demoPhotos/not-an-image.exe")
        .file
      it("Fails when parameter list are unequal") {
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
            albumIdList = listOf(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f5")),
            categoryIdList = listOf(UUID.fromString("3832ee5e-3f11-4f11-8189-56ca4f70f418"))
          )
        }
      }
    }
    describe("UploadPhotos()") {
      whenever(
        mockPhotoService.createNewMotiveAndSaveDigitalPhotos(
          motiveTitle = "Sindres dickpicks",
          placeName = "Storsalen",
          securityLevelId = mockDataService.generateSecurityLevelData().first().securityLevelId.id,
          photoGangBangerId = mockDataService.generatePhotoGangBangerData().first().photoGangBangerId.id,
          albumId = mockDataService.generateAlbumData().first().albumId.id,
          photoFileList = listOf(
            mock<MultipartFile> {},
            mock<MultipartFile> {}
          ),
          isGoodPhotoList = listOf(true, true),
          tagList = listOf(listOf("big dick!")),
          // TODO: Return full url instead?
          categoryName = "Gjengfoto",
          eventOwnerName = "Samfundet"
        )
      )
        .thenReturn(listOf("bildeUrl1.jpg", "bildeUrl2.jpg"))

      val response = photoController.uploadPhotos(
        motiveTitle = "Sindres dickpicks",
        placeName = "Storsalen",
        securityLevelId = mockDataService.generateSecurityLevelData().first().securityLevelId.id,
        photoGangBangerId = mockDataService.generatePhotoGangBangerData().first().photoGangBangerId.id,
        albumId = mockDataService.generateAlbumData().first().albumId.id,
        photoFileList = listOf<MultipartFile>(),
        isGoodPhotoList = listOf<Boolean>(),
        tagList = listOf<List<String>>(
          listOf("Sindre"),
          listOf("Enorm!")
        ),
        categoryName = "Gjengfoto",
        eventOwnerName = "Samfundet"
      )
      assertEquals(HttpStatus.CREATED, response.statusCode)
    }
/*
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

 */
  }
})
