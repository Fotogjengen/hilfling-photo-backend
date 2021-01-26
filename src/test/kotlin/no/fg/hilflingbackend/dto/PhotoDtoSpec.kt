package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.MockDataService
import no.fg.hilflingbackend.value_object.ImageFileName
import no.fg.hilflingbackend.value_object.PhotoSize
import no.fg.hilflingbackend.value_object.SecurityLevelType
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.springframework.core.io.ClassPathResource
import kotlin.test.assertEquals

class PhotoDtoSpec : Spek({
  describe("PhotoDto") {
    // Arrange
    val mockDataService = MockDataService()
    val photoFile = ClassPathResource("demoPhotos/digfø3652.jpg")
    val photoDto = mockDataService.generatePhoto()
      .first()


    describe("generateFilePath()") {
      // TODO: This test method should be moved to photoServiceSpec

      /*
      it("Generates correct path") {
        // Act
        val generatedPath = photoDto.getFilePath(
          fileName = ImageFileName("test.png"),
          securityLevel = SecurityLevelDto(securityLevelType = SecurityLevelType.ALLE),
          size = PhotoSize.Small,
          basePath = "static-files/static/img/"
        )
        // Asses
        assertEquals("/basepath/ALLE/Small-test.png", generatedPath.toString())
      }
       */
    }

    /*
    it("Generates the right filePath") {
      val securityLevel = mockDataService
        .generateSecurityLevelData().first()
      val fileName = ImageFileName(
        "TestImage.png"
      )
      // Act
      val path = PhotoDto.generateFilePath(
        fileName,

        securityLevel = SecurityLevel {
          id = UUID.randomUUID()
          type = SecurityLevelType.FG.name
        },
        SecurityLevelType.FG.name
      )
      // TODO: Use location from application.yml
      val expectedPath= "static-files/static/img/saved-photos-path/FG/$fileName"

      assertEquals(expectedPath, path)

    }
     */
    /*
   val photo = PhotoDto.createPhotoDtoAndSaveToDisk(
     photoFile = photoFile,
     isGoodPicture = true,
     motive = mockDataService.generateMotiveData().first(),
     placeDto = mockDataService.generatePlaceData().first(),
     securityLevel = mockDataService.generateSecurityLevelData().first(),
     gangDto = mockDataService.generateGangData().first(),
     photoGangBangerDto = mockDataService.generatePhotoTagData().first()
   )
     */
  }
})
