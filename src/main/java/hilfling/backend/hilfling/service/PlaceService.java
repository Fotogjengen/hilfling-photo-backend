package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.Place;
import hilfling.backend.hilfling.repositories.PlaceRepository;
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
