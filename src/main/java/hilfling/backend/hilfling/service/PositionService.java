package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.Position;
import hilfling.backend.hilfling.repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionService extends
        GenericBaseServiceImplementation<Position> {
    @Autowired
    PositionRepository repository;
    public PositionRepository getRepository() {
        return repository;
    }

}
