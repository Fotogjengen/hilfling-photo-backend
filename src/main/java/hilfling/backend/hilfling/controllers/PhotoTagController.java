package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.PhotoTag;
import hilfling.backend.hilfling.service.PhotoTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/photo_tags")
public class PhotoTagController extends GenericBaseControllerImplementation<PhotoTag> {
    @Autowired
    private PhotoTagService service;
    public PhotoTagService getService() {
        return service;
    }
}
