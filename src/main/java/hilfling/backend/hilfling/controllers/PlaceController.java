package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.Place;
import hilfling.backend.hilfling.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/places")
public class PlaceController extends GenericBaseControllerImplementation<Place> {
    @Autowired
    private PlaceService service;
    public PlaceService getService() {
        return service;
    }
}
