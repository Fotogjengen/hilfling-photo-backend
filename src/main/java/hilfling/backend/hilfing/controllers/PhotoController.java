package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.Photo;
import hilfling.backend.hilfing.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/photo")
public class PhotoController extends GenericBaseControllerImplementation<Photo> {
    @Autowired
    private PhotoService service;
    public PhotoService getService() {
        return service;
    }
}
