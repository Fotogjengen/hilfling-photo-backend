package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Position;
import hilfling.backend.hilfing.repositories.PositionRepository;
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
