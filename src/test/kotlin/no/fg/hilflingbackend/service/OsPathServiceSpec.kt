package no.fg.hilflingbackend.service

import junit.framework.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class OsPathServiceSpec : Spek({
  describe("OsPathService") {
    describe("convertToValidFolderName") {
      val osPathService = OsPathService()
      it("can convert string to valid folder name") {
        val foldername = osPathService.convertToValidFolderName("Vår 2018")
        val foldername2 = osPathService.convertToValidFolderName("Kul konsert på knaus")

        assertEquals("vaar_2018", foldername)
        assertEquals("kul_konsert_paa_knaus", foldername2)
      }
    }
  }
})
