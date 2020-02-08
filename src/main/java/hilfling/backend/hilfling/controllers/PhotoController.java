package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.Photo;
import hilfling.backend.hilfling.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/photo")
public class PhotoController extends GenericBaseControllerImplementation<Photo> {
    private static final Logger logger = LoggerFactory.getLogger(PhotoController.class);


    @Autowired
    private PhotoService service;
    public PhotoService getService() {
        return service;
    }

    @Autowired
    private PhotoService photoService;


    @Override
    public ResponseEntity<Photo> create(@Valid Photo entity) {
        return null; // Null response if missing photo file
    }

    @PostMapping("/upload")
    public ResponseEntity<Photo> create(
//            @Valid @RequestBody Photo entity,
            @RequestParam("file") MultipartFile file
    ) {
        String fileName = photoService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        /*entity.setLargeUrl(fileDownloadUri);
        entity.setMediumUrl(fileDownloadUri);
        entity.setSmallUrl(fileDownloadUri);*/
        System.out.println(fileName);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage() throws IOException {

        var imgFile = new ClassPathResource("./photos/eir1.png");
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = photoService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
