// package no.fg.hilflingbackend.exploration

// import com.azure.storage.blob.BlobServiceClientBuilder
// import com.azure.storage.blob.models.PublicAccessType
// import org.spekframework.spek2.Spek
// import org.spekframework.spek2.style.specification.describe
// import java.io.File
// import java.io.FileWriter
// import java.util.UUID
// import kotlin.test.assertTrue

// class HilflingBlobStorageExplorationSpec : Spek({
//   describe("BlobService client") {
//     it("Can create blob container") {
//       val connectionString = "DefaultEndpointsProtocol=http;AccountName=devstoreaccount1;AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;BlobEndpoint=http://127.0.0.1:10000/devstoreaccount1;QueueEndpoint=http://127.0.0.1:10001/devstoreaccount1;TableEndpoint=http://127.0.0.1:10002/devstoreaccount1;"
//       // BlobServiceClient.
//       println("connection string: $connectionString")

//       // Create a BlobServiceClient object which will be used to create a container client
//       val blobServiceClient = BlobServiceClientBuilder().connectionString(connectionString).buildClient()

//       println("properties")
//       println(blobServiceClient.properties)
//       // Create a unique name for the container
//       val containerName = "quickstartblobs" + UUID.randomUUID()

//       // Create the container and return a container client object
//       val containerClient = blobServiceClient.createBlobContainer(containerName)
//       // Allow public access to container
//       containerClient.setAccessPolicy(PublicAccessType.BLOB, null)

//       // Create a local file in the ./ directory for uploading and downloading
//       val localPath = "./"
//       val fileName = "quickstart" + UUID.randomUUID() + ".txt"
//       val localFile = File(localPath + fileName)

//       // Write text to the file
//       val writer = FileWriter(localPath + fileName, true)
//       writer.write("Hello, World!")
//       writer.close()

//       // Get a reference to a blob
//       val blobClient = containerClient.getBlobClient(fileName)

//       println("Uploading to Blob storage as blob:${blobClient.blobUrl}")

//       // Upload the blob
//       blobClient.uploadFromFile(localPath + fileName)
//       println("\nListing blobs...")
//       // List the blob(s) in the container.
//       blobClient.containerClient.listBlobs()

//       assertTrue {
//         containerClient.listBlobs().any {
//           it.name == fileName
//         }
//       }

//       // Clean up
//       containerClient.delete()
//       localFile.delete()
//     }
//   }
// })
