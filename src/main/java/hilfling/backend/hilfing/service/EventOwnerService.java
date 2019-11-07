package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.EventOwner;
import hilfling.backend.hilfing.repositories.EventOwnerRepository;
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
