package no.fg.hilflingbackend.blobStorage

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class AzureStorageConfiguration(
  @Value("\${spring.azure.storage.blob-storage.connection-string}")
  val azureStorageConnectionString: String
) {
}

fun main() {
}
