package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Gang;
import hilfling.backend.hilfing.repositories.GangRepository;
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
