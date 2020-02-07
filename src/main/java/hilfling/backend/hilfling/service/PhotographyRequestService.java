package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.PhotographyRequest;
import hilfling.backend.hilfling.repositories.PhotographyRequestRepository;
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
