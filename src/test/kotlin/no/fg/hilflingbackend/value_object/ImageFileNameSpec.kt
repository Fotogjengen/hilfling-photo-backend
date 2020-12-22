package no.fg.hilflingbackend.value_object

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class ImageFileNameSpec: Spek({
  describe("ImageFileName") {
    it("Correctly adds creates a valid filename") {
      val fileName = ImageFileName("picture.jpg")
      assertEquals("picture.jpg", fileName.filename)
      assertEquals("picture.png", ImageFileName("picture.png").filename)
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
