package no.fg.hilflingbackend.service

import com.nhaarman.mockitokotlin2.mock
import no.fg.hilflingbackend.configurations.ImageFileStorageProperties
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
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
  val placeRepository = mock<PlaceRepository> {}
  val securityLevelRepository = mock<SecurityLevelRepository> {}
  val photoGangBangerRepository = mock<PhotoGangBangerRepository> {}
  val environment = mock<Environment> {}
  describe("PhotoServiceSpec") {
    val photoService = PhotoService(
      imageFileStorageProperties = imageFileStorageProperties,
      environment = environment,
      photoRepository = photoRepository,
      gangRepository = gangRepository,
      placeRepository = placeRepository,
      securityLevelRepository = securityLevelRepository,
      photoGangBangerRepository = photoGangBangerRepository,
      motiveRepository = motiveRepository
    )
  }
})
