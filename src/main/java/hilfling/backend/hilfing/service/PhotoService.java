package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Photo;
import hilfling.backend.hilfing.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService extends
        GenericBaseServiceImplementation<Photo> {
    @Autowired
    PhotoRepository repository;
    public PhotoRepository getRepository() {
        return repository;
    }

}
