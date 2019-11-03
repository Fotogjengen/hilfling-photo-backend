package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.PhotoGangBanger;
import hilfling.backend.hilfing.repositories.PhotoGangBangerRepository;
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
