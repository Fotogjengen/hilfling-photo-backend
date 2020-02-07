package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.PhotoGangBangerPosition;
import hilfling.backend.hilfling.repositories.PhotoGangBangerPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoGangBangerPositionService extends
        GenericBaseServiceImplementation<PhotoGangBangerPosition> {
    @Autowired
    PhotoGangBangerPositionRepository repository;
    public PhotoGangBangerPositionRepository getRepository() {
        return repository;
    }

}
