package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.Place;
import hilfling.backend.hilfing.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
public class PlaceController {
    @Autowired
    private PlaceService placeService;

    @PostMapping
    public ResponseEntity<Place> createPlace(@Valid @RequestBody Place place) {
        return placeService.createPlace(place);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlace(@PathVariable("id") Long id) {
        return placeService.getPlace(id);
    }

    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@Valid @RequestBody Place place, @PathVariable("id") Long id) {
        return placeService.updatePlace(place, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable("id") Long id) {
        return placeService.deletePlace(id);
    }

}
