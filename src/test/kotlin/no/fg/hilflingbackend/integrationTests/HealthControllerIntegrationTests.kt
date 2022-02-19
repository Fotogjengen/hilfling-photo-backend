package no.fg.hilflingbackend.integrationTests

import no.fg.hilflingbackend.blobStorage.AzureStorageConfiguration
import no.fg.hilflingbackend.controller.HealthController
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@SpringBootTest()
class HealthControllerIntegrationTests {
  @Autowired
  lateinit var azureStorageConfiguration: AzureStorageConfiguration

  @Test
  fun exampleTest() {
    Assertions.assertThat(true).isEqualTo(true)
  }
}
