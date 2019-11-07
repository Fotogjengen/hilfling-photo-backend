package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Motive;
import hilfling.backend.hilfing.repositories.MotiveRepository;
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
