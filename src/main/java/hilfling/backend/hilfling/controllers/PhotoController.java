package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.Photo;
import hilfling.backend.hilfling.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/photo")
public class PhotoController extends GenericBaseControllerImplementation<Photo> {
    private static final Logger logger = LoggerFactory.getLogger(PhotoController.class);
    private static List<String> ALLOWED_MEDIA_TYPES;

    static { // Allowed photo media types defined here
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(MediaType.IMAGE_JPEG.toString());
        tmp.add(MediaType.IMAGE_PNG.toString());
        ALLOWED_MEDIA_TYPES = Collections.unmodifiableList(tmp);
    }


    @Autowired
    private PhotoService service;

    public PhotoService getService() {
        return service;
    }

    @Autowired
    private PhotoService photoService;


    @Override
    public ResponseEntity<Photo> create(@Valid Photo entity) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Illegal response if missing photo file
    }


    @PostMapping("/upload")
    public ResponseEntity<Photo> create(
            @RequestPart("file") MultipartFile file,
            @RequestPart("entity") Photo entity
    ) {
        if (!entity.isValid()) {
            // Photo missing/having extra fields, e.q. not allowed
            // return Unprocessable entity
            return ResponseEntity.status(422).build();
        }
        if (!ALLOWED_MEDIA_TYPES.contains(file.getContentType())) {
            // File not in ALLOWED_MEDIA_TYPES, return unsupported media type
            return ResponseEntity.status(415).build();
        }
        Photo photo = getService().storePhoto(file, entity);
        return getService().create(photo);
    }

    @GetMapping("/download/{album}/{motive}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String album,
            @PathVariable String motive,
            @PathVariable String fileName,
            HttpServletRequest request
    ) {
        // Load file as Resource
        Resource resource = photoService.loadLargePhotoAsResource(
                album + "/" + motive + "/" + fileName
        );
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}