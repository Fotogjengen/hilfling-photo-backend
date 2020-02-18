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

import java.io.IOException;
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

    private final String relativeLargePhotoStorageLocation;
    private final String relativeMediumPhotoStorageLocation;
    private final String relativeSmallPhotoStorageLocation;

    private final Path absoluteLargePhotoStorageLocation;
    private final Path absoluteMediumPhotoStorageLocation;
    private final Path absoluteSmallPhotoStorageLocation;

    @Autowired
    public PhotoService(FileStorageProperties fileStorageProperties) {
        // Create relative paths
        this.relativeLargePhotoStorageLocation = fileStorageProperties.getLargeUploadDir();
        this.relativeMediumPhotoStorageLocation = fileStorageProperties.getMediumUploadDir();
        this.relativeSmallPhotoStorageLocation = fileStorageProperties.getSmallUploadDir();

        // Create absolute paths
        this.absoluteLargePhotoStorageLocation = Paths.get(relativeLargePhotoStorageLocation)
                .toAbsolutePath().normalize();
        this.absoluteMediumPhotoStorageLocation = Paths.get(relativeMediumPhotoStorageLocation)
                .toAbsolutePath().normalize();
        this.absoluteSmallPhotoStorageLocation = Paths.get(relativeSmallPhotoStorageLocation)
                .toAbsolutePath().normalize();
        try {
            // Create directories (if not exists)
            Files.createDirectories(this.absoluteLargePhotoStorageLocation);
            Files.createDirectories(this.absoluteMediumPhotoStorageLocation);
            Files.createDirectories(this.absoluteSmallPhotoStorageLocation);
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
                    "album_" + photo.getMotive().getAlbum().getId()
                            + "/motive_" + photo.getMotive().getId()
                            + "/" + fileName
            );
            // Copy file to the target location (Replacing existing file with the same name)
            Path largeTargetLocation = this.absoluteLargePhotoStorageLocation.resolve(imagePath);
            Path mediumTargetLocation = this.absoluteMediumPhotoStorageLocation.resolve(imagePath);
            Path smallTargetLocation = this.absoluteSmallPhotoStorageLocation.resolve(imagePath);

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

            photo.setLargeUrl(relativeLargePhotoStorageLocation + imagePath);
            photo.setMediumUrl(relativeMediumPhotoStorageLocation + imagePath);
            photo.setSmallUrl(relativeSmallPhotoStorageLocation + imagePath);

            return photo;
        } catch (IOException exception) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", exception);
        }
    }

    public Resource loadLargePhotoAsResource(String fileName) {
        try {
            Path filePath = this.absoluteLargePhotoStorageLocation.resolve(fileName).normalize();
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
