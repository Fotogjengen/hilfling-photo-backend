package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.Gang;
import hilfling.backend.hilfling.repositories.GangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GangService extends
        GenericBaseServiceImplementation<Gang> {
    @Autowired
    GangRepository repository;
    public GangRepository getRepository() {
        return repository;
    }

};
