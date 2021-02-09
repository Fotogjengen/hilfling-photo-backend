package no.fg.hilflingbackend.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.stereotype.Component
/*
@ConstructorBinding
@EnableConfigurationProperties
@Component
@ConfigurationProperties(prefix = "file")
data class FileStorageProperties (
        val largeUploadDir: String,
        val mediumUploadDir: String,
        val smallUploadDir: String
)

 */
@ConfigurationPropertiesScan
@Component
@ConfigurationProperties(prefix = "static-files.img")
data class ImageFileStorageProperties(
  // Default save location for images
  val savedPhotosPath: String = "static-files/static/img" // TODO: Make this work!
)
