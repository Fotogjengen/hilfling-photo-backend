package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.PhotoGangBanger;
import hilfling.backend.hilfling.repositories.PhotoGangBangerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoGangBangerService extends
        GenericBaseServiceImplementation<PhotoGangBanger> {
    @Autowired
    PhotoGangBangerRepository repository;
    public PhotoGangBangerRepository getRepository() {
        return repository;
    }

}
