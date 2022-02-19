package no.fg.hilflingbackend.repository

import no.fg.hilflingbackend.blobStorage.AzureBlobStorage
import no.fg.hilflingbackend.blobStorage.AzureStorageConfiguration
import no.fg.hilflingbackend.value_object.ImageFileName
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringRunner
import java.util.UUID
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@SpringBootTest()
internal class AzureBlobStorageTest {

  @Autowired
  lateinit var azureStorageConfiguration: AzureStorageConfiguration
  lateinit var azureBlobStorage: AzureBlobStorage
  lateinit var containerName: String

  @BeforeEach
  fun setup_test_blob_storage() {
    azureBlobStorage = AzureBlobStorage(azureStorageConfiguration)
    // Create a unique name for the container
    containerName = "test" + UUID.randomUUID()

    azureBlobStorage.blobServiceClient.createBlobContainer(containerName)
  }

  @AfterEach
  fun tearDown_test_blob_storage() {
    azureBlobStorage.blobServiceClient.deleteBlobContainer(containerName)
  }

  @Test
  fun shouldSaveFile() {
    val mockFile = MockMultipartFile("file", "test.png", "text/plain", "some xml".toByteArray())

    val response = azureBlobStorage.saveFile(
      multipartFile = mockFile,
      blobContainerName = containerName,
      fileName = ImageFileName("test.png")
    )

    assertTrue(response.contains("test.png"))
  }
}
