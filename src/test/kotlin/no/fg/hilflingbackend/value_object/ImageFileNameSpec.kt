package no.fg.hilflingbackend.value_object

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ImageFileNameSpec : Spek({
  describe("ImageFileName") {
    val fileName = ImageFileName("picture.jpg")
    it("Correctly adds creates a valid filename") {
      assertEquals("picture.jpg", fileName.filename)
      assertEquals("digfø3652.jpg", ImageFileName("digfø3652.jpg").filename)
      assertEquals("picture.png", ImageFileName("picture.png").filename)
    }
    it("Generates the right fileExtension") {
      assertEquals(".jpg", fileName.getFileExtension())
    }
    it("Fails on wrong input") {
      assertFailsWith<IllegalArgumentException> {
        ImageFileName("notApicture.pdf")
      }
      assertFailsWith<IllegalArgumentException> {
        ImageFileName("notApicture.exe")
      }
      assertFailsWith<IllegalArgumentException> {
        ImageFileName("notApicture.png.png")
      }
    }
  }
})
