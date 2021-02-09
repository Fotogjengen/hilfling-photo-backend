package no.fg.hilflingbackend.value_object

import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

class ZipCodeSpec : Spek({
  describe("Create ZipCode test") {
    it("Should fail, zip code too long") {
      assertThrows<IllegalArgumentException> { ZipCode("12345") }
    }
    it("Should fail, not a zip code") {
      assertThrows<IllegalArgumentException> {ZipCode("oh no")}
    }
    it("Should succeed, is a zip code") {
      val zipCode = ZipCode("1472")
      assertEquals(zipCode.value, "1472")
    }
  }
})
