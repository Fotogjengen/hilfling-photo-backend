package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.Photo;
import hilfling.backend.hilfling.service.PhotoService;
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
