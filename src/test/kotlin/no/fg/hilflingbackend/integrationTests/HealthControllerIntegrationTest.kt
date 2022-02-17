package no.fg.hilflingbackend.integrationTests

import no.fg.hilflingbackend.blobStorage.AzureStorageConfiguration
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest()
@TestPropertySource(locations= ["classpath:test.yaml"])
class HealthControllerIntegrationTest {
  @Autowired
  val azureStorageConfiguration: AzureStorageConfiguration

  @Test
  fun exampleTest() {
    Assertions.assertThat(true).isEqualTo(true)
  }
}
