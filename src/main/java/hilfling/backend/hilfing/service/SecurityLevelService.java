package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.SecurityLevel;
import hilfling.backend.hilfing.repositories.SecurityLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityLevelService extends
        GenericBaseServiceImplementation<SecurityLevel> {

    @Autowired
    SecurityLevelRepository repository;
    public SecurityLevelRepository getRepository() {
        return repository;
    }
}
