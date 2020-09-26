package hilfling.backend.hilfling.configurations

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "file")
data class FileStorageProperties (
        val largeUploadDir: String,
        val mediumUploadDir: String,
        val smallUploadDir: String
)
