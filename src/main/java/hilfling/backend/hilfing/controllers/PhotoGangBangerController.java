package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.PhotoGangBanger;
import hilfling.backend.hilfing.service.PhotoGangBangerService;
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
