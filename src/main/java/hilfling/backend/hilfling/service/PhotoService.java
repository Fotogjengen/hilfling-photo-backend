package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.configurations.FileStorageProperties;
import hilfling.backend.hilfling.exceptions.FileNotFoundException;
import hilfling.backend.hilfling.exceptions.FileStorageException;
import hilfling.backend.hilfling.model.Photo;
import hilfling.backend.hilfling.repositories.PhotoRepository;
import hilfling.backend.hilfling.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class PhotoService extends
        GenericBaseServiceImplementation<Photo> {
    @Autowired
    PhotoRepository repository;

    public PhotoRepository getRepository() {
        return repository;
    }

    private final String baseLargePhotoStorageLocation;
    private final String baseMediumPhotoStorageLocation;
    private final String baseSmallPhotoStorageLocation;

    private final Path largePhotoStorageLocation;
    private final Path mediumPhotoStorageLocation;
    private final Path smallPhotoStorageLocation;

    @Autowired
    public PhotoService(FileStorageProperties fileStorageProperties) {
        this.baseLargePhotoStorageLocation = fileStorageProperties.getLargeUploadDir();
        this.baseMediumPhotoStorageLocation = fileStorageProperties.getMediumUploadDir();
        this.baseSmallPhotoStorageLocation = fileStorageProperties.getSmallUploadDir();

        this.largePhotoStorageLocation = Paths.get(baseLargePhotoStorageLocation)
                .toAbsolutePath().normalize();
        this.mediumPhotoStorageLocation = Paths.get(baseMediumPhotoStorageLocation)
                .toAbsolutePath().normalize();
        this.smallPhotoStorageLocation = Paths.get(baseSmallPhotoStorageLocation)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.largePhotoStorageLocation);
            Files.createDirectories(this.mediumPhotoStorageLocation);
            Files.createDirectories(this.smallPhotoStorageLocation);
        } catch (Exception exception) {
            throw new FileStorageException(
                    "Could not create the directory where the uploaded files will be stored.",
                    exception
            );
        }
    }

    @Override
    public ResponseEntity<Photo> create(Photo entity) {
        if (entity.getId() != null) {
            return ResponseEntity.status(403).build();
        }
        try {
            getRepository().save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception error) {
            return ResponseEntity.status(304).build();
        }
    }

    public Photo storePhoto(MultipartFile file, Photo photo) {
        // Normalize filename
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence " + fileName);
            }
            Path imagePath = Paths.get(
                    "album_" + photo.getMotive().getAlbum()
                            + "/motive_" + photo.getMotive().getId()
                            + "/" + fileName
            );
            // Copy file to the target location (Replacing existing file with the same name)
            Path largeTargetLocation = this.largePhotoStorageLocation.resolve(imagePath);
            Path mediumTargetLocation = this.mediumPhotoStorageLocation.resolve(imagePath);
            Path smallTargetLocation = this.smallPhotoStorageLocation.resolve(imagePath);

            // Create directories if not exists
            Files.createDirectories(largeTargetLocation);
            Files.createDirectories(mediumTargetLocation);
            Files.createDirectories(smallTargetLocation);

            Files.copy(file.getInputStream(), largeTargetLocation, StandardCopyOption.REPLACE_EXISTING); // Full size
            Files.copy(ImageUtils.resizeImage(
                    file, 600
            ), mediumTargetLocation, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(ImageUtils.resizeImage(
                    file, 200
            ), smallTargetLocation, StandardCopyOption.REPLACE_EXISTING);

            photo.setLargeUrl(baseLargePhotoStorageLocation + imagePath);
            photo.setMediumUrl(baseMediumPhotoStorageLocation + imagePath);
            photo.setSmallUrl(baseSmallPhotoStorageLocation + imagePath);

            return photo;
        } catch (IOException exception) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", exception);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.largePhotoStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}
