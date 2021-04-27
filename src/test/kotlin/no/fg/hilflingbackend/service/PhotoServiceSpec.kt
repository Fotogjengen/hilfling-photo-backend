package no.fg.hilflingbackend.service

import com.nhaarman.mockitokotlin2.mock
import no.fg.hilflingbackend.configurations.ImageFileStorageProperties
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.CategoryRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.PhotoTagRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.springframework.core.env.Environment

class PhotoServiceSpec : Spek({
  val motiveRepository = mock<MotiveRepository> {
  }
  // Arrange mocks
  val imageFileStorageProperties = mock<ImageFileStorageProperties> {}
  val photoRepository = mock<PhotoRepository> {}
  val gangRepository = mock<GangRepository> {}
  val albumRepository = mock<AlbumRepository> {}
  val categoryRepository = mock<CategoryRepository> {}
  val placeRepository = mock<PlaceRepository> {}
  val securityLevelRepository = mock<SecurityLevelRepository> {}
  val photoGangBangerRepository = mock<PhotoGangBangerRepository> {}
  val eventOwnerReposity = mock<EventOwnerRepository> {}
  val photoTagRepository = mock<PhotoTagRepository>{}
  val environment = mock<Environment> {}
  describe("PhotoServiceSpec") {
    /*

  val log = LoggerFactory.getLogger(this::class.java)
  val mockDataService = MockDataService()

  describe("PhotoService") {
    // Arrange
    val mockPhoto = mockDataService.generatePhoto().first()
    val album = mockDataService.generateAlbumData().first()
    val motive = mock<MotiveRepository> {
      on { findById(UUID.randomUUID()) } doReturn Motive { title = "test" }
    }
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
      on { findById(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f8")) } doReturn mockDataService
        .generateAlbumData()
        .first()
    }
    val photoTagRepository = mock<PhotoTagRepository> {}
    val categoryRepository = mock<CategoryRepository> {}
    val eventOwnerRepository = mock<EventOwnerRepository> {}
    val placeRepository = mock<PlaceRepository> {
      on { findById(mockDataService.generatePlaceData()[0].placeId.id) } doReturn mockDataService.generatePlaceData()[0]
      on { findById(mockDataService.generatePlaceData()[1].placeId.id) } doReturn mockDataService.generatePlaceData()[1]
    }
    val gangRepository = mock<GangRepository> {
      on { findById(mockDataService.generateGangData()[0].gangId.id) } doReturn mockDataService.generateGangData()[0]
      on { findById(mockDataService.generateGangData()[1].gangId.id) } doReturn mockDataService.generateGangData()[1]
    }
    val photoRepository = mock<PhotoRepository> {
      on { findById(mockPhoto.photoId.id) } doReturn mockPhoto
    }
    val securityLevelRepository = mock<SecurityLevelRepository> {
      on { findById(mockDataService.generateSecurityLevelData()[0].securityLevelId.id) } doReturn mockDataService.generateSecurityLevelData()[0]
      on { findById(mockDataService.generateSecurityLevelData()[1].securityLevelId.id) } doReturn mockDataService.generateSecurityLevelData()[1]
    }

    val photoFile = ClassPathResource("demoPhotos/digfø3652.jpg")
      .file
    val multiPartFiles = listOf(
      MockMultipartFile("file", photoFile.name, "text/plain", photoFile.inputStream()),
      MockMultipartFile("file", photoFile.name, "text/plain", photoFile.inputStream())
    )
    val bindingResult = mock<BindingResult>()

    val environment = mock<Environment> {}
    val imageStaticFilesProperties = mock<ImageFileStorageProperties> {
      on { savedPhotosPath } doReturn "static-files/static/img"
    }

     */
    val photoService = PhotoService(
      imageFileStorageProperties = imageFileStorageProperties,
      environment = environment,
      photoRepository = photoRepository,
      gangRepository = gangRepository,
      placeRepository = placeRepository,
      securityLevelRepository = securityLevelRepository,
      photoGangBangerRepository = photoGangBangerRepository,
      motiveRepository = motiveRepository,
      albumRepository = albumRepository,
      categoryRepository = categoryRepository,
      eventOwnerRepository = eventOwnerReposity,
      photoTagRepository = photoTagRepository
    )
  }
})
