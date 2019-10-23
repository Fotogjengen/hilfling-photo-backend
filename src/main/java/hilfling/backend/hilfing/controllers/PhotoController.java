package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.Photo;
import hilfling.backend.hilfing.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/photo")
public class PhotoController {
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping()
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    @PostMapping()
    public Photo createPhoto(@Valid @RequestBody Photo photo) {
        return photoRepository.save(photo);
    }

}
