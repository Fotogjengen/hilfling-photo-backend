package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.PhotographyRequest;
import hilfling.backend.hilfing.repositories.PhotographyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotographyRequestService extends
        GenericBaseServiceImplementation<PhotographyRequest> {
    @Autowired
    PhotographyRequestRepository repository;
    public PhotographyRequestRepository getRepository() {
        return repository;
    }

}
