// package no.fg.hilflingbackend.dto

// import no.fg.hilflingbackend.MockDataService
// import org.spekframework.spek2.Spek
// import org.spekframework.spek2.style.specification.describe
// import org.springframework.core.io.ClassPathResource

// class PhotoDtoSpec : Spek({
//   describe("PhotoDto") {
//     // Arrange
//     val mockDataService = MockDataService()
//     val photoFile = ClassPathResource("demoPhotos/digfø3652.jpg")

//     /*
//     it("Generates the right filePath") {
//       val securityLevel = mockDataService
//         .generateSecurityLevelData().first()
//       val fileName = ImageFileName(
//         "TestImage.png"
//       )
//       // Act
//       val path = PhotoDto.generateFilePath(
//         fileName,

//         securityLevel = SecurityLevel {
//           id = UUID.randomUUID()
//           type = SecurityLevelType.FG.name
//         },
//         SecurityLevelType.FG.name
//       )
//       // TODO: Use location from application.yml
//       val expectedPath= "static-files/static/img/saved-photos-path/FG/$fileName"

//       assertEquals(expectedPath, path)

//     }
//      */
//     /*
//    val photo = PhotoDto.createPhotoDtoAndSaveToDisk(
//      photoFile = photoFile,
//      isGoodPicture = true,
//      motive = mockDataService.generateMotiveData().first(),
//      placeDto = mockDataService.generatePlaceData().first(),
//      securityLevel = mockDataService.generateSecurityLevelData().first(),
//      gangDto = mockDataService.generateGangData().first(),
//      photoGangBangerDto = mockDataService.generatePhotoTagData().first()
//    )
//      */
//   }
// })
