package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.Album;
import hilfling.backend.hilfling.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumService extends
        GenericBaseServiceImplementation<Album> {
    @Autowired
    AlbumRepository repository;
    public AlbumRepository getRepository() {
        return repository;
    }

}
