package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.PhotoGangBangerPosition;
import hilfling.backend.hilfing.repositories.PhotoGangBangerPositionRepository;
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
