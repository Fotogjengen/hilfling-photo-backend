package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.PhotoTag;
import hilfling.backend.hilfling.repositories.PhotoTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoTagService extends
        GenericBaseServiceImplementation<PhotoTag> {
    @Autowired
    PhotoTagRepository repository;
    public PhotoTagRepository getRepository() {
        return repository;
    }

}
