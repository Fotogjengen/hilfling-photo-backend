package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.Motive;
import hilfling.backend.hilfling.repositories.MotiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotiveService extends
        GenericBaseServiceImplementation<Motive> {
    @Autowired
    MotiveRepository repository;
    public MotiveRepository getRepository() {
        return repository;
    }

}
