package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Place;
import hilfling.backend.hilfing.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {
    @Autowired
    PlaceRepository placeRepository;

    public ResponseEntity<Place> createPlace(Place place) {
        try {
            Place savedPlace = placeRepository.save(place);
            return ResponseEntity.ok().body(savedPlace);
        } catch (Exception e) {
            return ResponseEntity.status(304).build();
        }
    }

    public ResponseEntity<Place> getPlace(Long id) {
        return placeRepository.findById(id)
                .map(place -> ResponseEntity.ok().body(place))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        return ResponseEntity.ok().body(places);
    }

    public ResponseEntity<Place> updatePlace(Place place, Long id) {
        if (placeRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        place.setId(id);
        return ResponseEntity.ok().body(placeRepository.save(place));
    }

    public ResponseEntity<?> deletePlace(Long id) {
        return placeRepository.findById(id)
                .map(place -> {
                    placeRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
