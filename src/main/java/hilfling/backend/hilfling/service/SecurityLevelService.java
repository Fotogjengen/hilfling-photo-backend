package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.SecurityLevel;
import hilfling.backend.hilfling.repositories.SecurityLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityLevelService extends
        GenericBaseServiceImplementation<SecurityLevel> {

    @Autowired
    SecurityLevelRepository repository;
    public SecurityLevelRepository getRepository() {
        return repository;
    }
}
