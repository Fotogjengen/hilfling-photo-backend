package no.fg.hilflingbackend.integrationTests

import no.fg.hilflingbackend.service.PhotoService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertTrue

class PhotoServiceIntegrationTest(@Autowired private val photoService: PhotoService) {
  @Test
  fun test() {
    assertTrue(true)
  }
}
