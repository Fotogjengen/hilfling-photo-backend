package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.configurations.FileStorageProperties;
import hilfling.backend.hilfling.exceptions.FileNotFoundException;
import hilfling.backend.hilfling.exceptions.FileStorageException;
import hilfling.backend.hilfling.model.Photo;
import hilfling.backend.hilfling.repositories.PhotoRepository;
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

    private final Path photoStorageLocation;

    @Autowired
    public PhotoService(FileStorageProperties fileStorageProperties) {
        this.photoStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.photoStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
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

    public String storeFile(MultipartFile file) {
        // Normalize filename
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.photoStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException exception) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", exception);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.photoStorageLocation.resolve(fileName).normalize();
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
