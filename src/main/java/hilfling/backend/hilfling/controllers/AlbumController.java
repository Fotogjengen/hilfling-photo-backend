package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.Album;
import hilfling.backend.hilfling.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController extends GenericBaseControllerImplementation<Album> {
    @Autowired
    private AlbumService service;
    public AlbumService getService() {
        return service;
    }

}
