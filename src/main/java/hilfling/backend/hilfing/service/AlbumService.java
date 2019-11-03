package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Album;
import hilfling.backend.hilfing.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
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
