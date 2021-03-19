package no.fg.hilflingbackend.utils

import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FolderPathUtilsSpec : Spek({
  describe("OsPathService") {
    describe("convertToValidFolderName") {
      it("can convert string to valid folder name") {
        val foldername = convertToValidFolderName("Vår 2018")
        val foldername2 = convertToValidFolderName("Kul konsert på knaus")

        assertEquals("vaar_2018", foldername)
        assertEquals("kul_konsert_paa_knaus", foldername2)
      }
    }

    describe("createFolder") {
      val basePath = "static-files/static/img/"
      it("can create folder") {
        val returnedPath = createFolder(basePath, "test")
        assertTrue(
          Files.isDirectory(
            Paths.get(basePath + "test")
          )
        )
        Files.delete(
          Paths.get(basePath + "test")
        )
      }
      it("Can not create folder if folder exist") {
        val returnedPath = createFolder(basePath, "test")
        assertThrows<IOException> {
          createFolder(basePath, "test")
        }
        Files.delete(
          Paths.get(basePath + "test")
        )
      }
    }
  }
})
