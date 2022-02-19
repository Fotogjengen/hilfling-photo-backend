package no.fg.hilflingbackend.blobStorage

import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import no.fg.hilflingbackend.value_object.ImageFileName
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile

@Repository
class AzureBlobStorage(val azureStorageConfiguration: AzureStorageConfiguration) : IAzureBlobStorage {

  val blobServiceClient: BlobServiceClient = BlobServiceClientBuilder()
    .connectionString(azureStorageConfiguration.azureStorageConnectionString)
    .buildClient()

  override fun saveFile(multipartFile: MultipartFile, blobContainerName: String, fileName: ImageFileName): String {
    // Have to get a blob container client
    val blobContainerClient = this.blobServiceClient.getBlobContainerClient(blobContainerName)
    // Create blob client
    val blobClient = blobContainerClient.getBlobClient(fileName.filename)
    // Upload file
    blobClient.upload(multipartFile.inputStream, multipartFile.size,)

    return blobClient.blobUrl
  }
}
