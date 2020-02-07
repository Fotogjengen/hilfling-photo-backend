package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.Photo;
import hilfling.backend.hilfling.repositories.PhotoRepository;
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
