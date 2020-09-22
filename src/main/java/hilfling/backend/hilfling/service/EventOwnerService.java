package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.EventOwner;
import hilfling.backend.hilfling.repositories.EventOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventOwnerService extends
        GenericBaseServiceImplementation<EventOwner> {
    @Autowired
    EventOwnerRepository repository;
    public EventOwnerRepository getRepository() {
        return repository;
    }

}
