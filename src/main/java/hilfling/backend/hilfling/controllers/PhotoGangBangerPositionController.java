package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.PhotoGangBangerPosition;
import hilfling.backend.hilfling.service.PhotoGangBangerPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/photo_gang_banger_positions")
public class PhotoGangBangerPositionController extends GenericBaseControllerImplementation<PhotoGangBangerPosition> {
    @Autowired
    private PhotoGangBangerPositionService service;
    public PhotoGangBangerPositionService getService() {
        return service;
    }
}
