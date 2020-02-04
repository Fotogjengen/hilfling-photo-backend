package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.PhotoTag;
import hilfling.backend.hilfing.repositories.PhotoTagRepository;
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
