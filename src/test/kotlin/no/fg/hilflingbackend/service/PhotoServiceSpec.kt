package no.fg.hilflingbackend.service

import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import no.fg.hilflingbackend.repository.AlbumRepository
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on
import com.nhaarman.mockitokotlin2.anyVararg
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import no.fg.hilflingbackend.MockDataService
import no.fg.hilflingbackend.configurations.ImageStaticFilesProperties
import no.fg.hilflingbackend.controller.PhotoController
import no.fg.hilflingbackend.repository.CategoryRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.PhotoTagRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import no.fg.hilflingbackend.service.PhotoService
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import org.spekframework.spek2.style.specification.describe
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.validation.BindingResult
import java.io.IOException
import java.security.InvalidParameterException
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PhotoServiceSpec: Spek({

  val log = LoggerFactory.getLogger(this::class.java)
  val mockDataService = MockDataService()

  describe("PhotoService") {
  // Arrange
  val mockPhoto = mockDataService.generatePhoto().first()
  val album = mockDataService.generateAlbumData().first()
  val photoGangBangerRepository = mock<PhotoGangBangerRepository> {
    on { findById(mockDataService.generatePhotoGangBangerData()[0].photoGangBangerId.id) } doReturn mockDataService.generatePhotoGangBangerData()[0]
    on { findById(mockDataService.generatePhotoGangBangerData()[1].photoGangBangerId.id) } doReturn mockDataService.generatePhotoGangBangerData()[1]
  }
  val motiveRepository = mock<MotiveRepository> {
    on { findById(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc4bcd")) } doReturn mockDataService
      .generateMotiveData().first()
    on { findById(mockDataService.generateMotiveData()[1].id) } doReturn mockDataService
      .generateMotiveData()[1]
  }
  val albumRepository: AlbumRepository = mock<AlbumRepository> {
    on { runBlocking { findById(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f8"))}} doReturn mockDataService
      .generateAlbumData()
      .first()
  }
  val photoTagRepository = mock<PhotoTagRepository> {}
  val categoryRepository = mock<CategoryRepository> {}
  val eventOwnerRepository = mock<EventOwnerRepository> {}
  val placeRepository = mock<PlaceRepository> {
    on { runBlocking { findById(mockDataService.generatePlaceData()[0].placeId.id) } } doReturn mockDataService.generatePlaceData()[0]
    on { runBlocking { findById(mockDataService.generatePlaceData()[1].placeId.id) } } doReturn mockDataService.generatePlaceData()[1]
  }
  val gangRepository = mock<GangRepository> {
    on { runBlocking { findById(mockDataService.generateGangData()[0].gangId.id) } } doReturn mockDataService.generateGangData()[0]
    on { runBlocking { findById(mockDataService.generateGangData()[1].gangId.id) } } doReturn mockDataService.generateGangData()[1]
  }
  val photoRepository = mock<PhotoRepository> {
    on { findById(mockPhoto.photoId.id) } doReturn mockPhoto
  }
  val securityLevelRepository = mock<SecurityLevelRepository> {
    on { findById(mockDataService.generateSecurityLevelData()[0].securityLevelId.id) } doReturn mockDataService.generateSecurityLevelData()[0].toEntity()
    on { findById(mockDataService.generateSecurityLevelData()[1].securityLevelId.id) } doReturn mockDataService.generateSecurityLevelData()[1].toEntity()
  }

  val photoFile = ClassPathResource("demoPhotos/digfø3652.jpg")
    .file
  val multiPartFiles = listOf(
    MockMultipartFile("file", photoFile.name, "text/plain", photoFile.inputStream()),
    MockMultipartFile("file", photoFile.name, "text/plain", photoFile.inputStream())
  )
  val bindingResult = mock<BindingResult>()

    val environment = mock<Environment> {}
    val imageStaticFilesProperties = mock<ImageStaticFilesProperties>{
      on { savedPhotosPath } doReturn "static-files/static/img"
    }
    val photoService = PhotoService(
      imageStaticFilesProperties,
      environment,
      photoRepository,
      gangRepository,
      motiveRepository,
      placeRepository,
      securityLevelRepository,
      photoGangBangerRepository,
    )
  describe("getById()") {
    runBlockingTest {
      // Act
      val photo = photoService.getById(mockPhoto.photoId.id)

      it("Returns photo on request") {
        // Assert
        assertEquals(photo, mockPhoto)
      }
    }
  }

  describe("uploadPhoto()") {
    runBlockingTest {
      // Arrange
      try {

        // Act
        val response = photoService.saveDigitalPhotos(
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
          fileList = multiPartFiles,
        )

        // Assert
        it("Can create photo") {
          assertEquals(response.size, 2)
          assertTrue(response[0].contains(".jpg"))
          assertTrue(response[1].contains(".jpg"))
        }
      } catch (ex: IOException) {
        log.error(ex.localizedMessage)
      }
      it("Fails when file is not an image") {
        runBlockingTest {
          val notAPhotoFile = ClassPathResource("demoPhotos/not-an-image.exe")
            .file
          val multipartFile =
            MockMultipartFile("file", notAPhotoFile.name, "text/plain", notAPhotoFile.inputStream())
          assertThrows<IllegalArgumentException> {
            photoService.saveDigitalPhotos(
              isGoodPictureList = listOf(false),
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
    }
  }
  }
})
