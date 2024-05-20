// package no.fg.hilflingbackend.value_object

// import org.spekframework.spek2.Spek
// import org.spekframework.spek2.style.specification.describe
// import java.lang.IllegalArgumentException
// import kotlin.test.assertEquals
// import kotlin.test.assertFailsWith

// class ImageFileNameSpec : Spek({
//   describe("ImageFileName") {
//     val fileName = ImageFileName("picture.jpg")
//     it("Correctly adds creates a valid filename") {
//       assertEquals("picture.jpg", fileName.filename)
//       assertEquals("digfø3652.jpg", ImageFileName("digfø3652.jpg").filename)
//       assertEquals("picture.png", ImageFileName("picture.png").filename)
//     }
//     it("Generates the right fileExtension") {
//       assertEquals(".jpg", fileName.getFileExtension())
//     }
//     it("Works with filenamesV that has spaces") {
//       assertEquals("test space.jpg", ImageFileName("test space.jpg").filename)
//     }
//     it("Works with filenamesV that has . in it") {
//       assertEquals("Screenshot 2021-05-10 at 21.33.52.jpg", ImageFileName("Screenshot 2021-05-10 at 21.33.52.jpg").filename)
//     }
//     it("Fails on not whitelisted file extension") {
//       assertFailsWith<IllegalArgumentException> {
//         ImageFileName("notApicture.pdf")
//       }
//       assertFailsWith<IllegalArgumentException> {
//         ImageFileName("notApicture.exe")
//       }
//     }
//   }
// })
