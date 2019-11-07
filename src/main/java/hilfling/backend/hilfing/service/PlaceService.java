package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Place;
import hilfling.backend.hilfing.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaceService extends
        GenericBaseServiceImplementation<Place> {
    @Autowired
    PlaceRepository repository;
    public PlaceRepository getRepository() {
        return repository;
    }
}
