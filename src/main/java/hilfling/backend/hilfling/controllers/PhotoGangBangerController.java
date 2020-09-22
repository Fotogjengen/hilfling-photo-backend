package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.PhotoGangBanger;
import hilfling.backend.hilfling.service.PhotoGangBangerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/photo_gang_bangers")
public class PhotoGangBangerController extends GenericBaseControllerImplementation<PhotoGangBanger> {
    @Autowired
    private PhotoGangBangerService service;
    public PhotoGangBangerService getService() {
        return service;
    }
}
