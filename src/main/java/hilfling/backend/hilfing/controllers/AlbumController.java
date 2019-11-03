package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.Album;
import hilfling.backend.hilfing.service.AlbumService;
import hilfling.backend.hilfing.service.GenericBaseServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController extends GenericBaseControllerImplementation<Album> {
    @Autowired
    private AlbumService service;
    public AlbumService getService() {
        return service;
    }

}
