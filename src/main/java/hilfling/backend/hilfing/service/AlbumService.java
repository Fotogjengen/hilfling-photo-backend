package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Album;
import hilfling.backend.hilfing.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public ResponseEntity<Album> createAlbum(Album album) {
        try {
            Album savedAlbum = albumRepository.save(album);
            return ResponseEntity.ok().body(savedAlbum);
        } catch (Exception e) {
            return ResponseEntity.status(304).build();
        }
    }

    public ResponseEntity<Album> updateAlbum(Album albumDetails, Long id) {
        return albumRepository.findById(id)
                .map(album -> {
                    album.setTitle(albumDetails.getTitle());
                    album.setAnalog(albumDetails.getAnalog());
                    Album updatedAlbum = albumRepository.save(album);
                    return ResponseEntity.ok().body(updatedAlbum);
                }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Album> getAlbum(Long id) {
        return albumRepository.findById(id)
                .map(album -> ResponseEntity.ok().body(album))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = albumRepository.findAll();
        return ResponseEntity.ok().body(albums);
    }

    public ResponseEntity<?> deleteAlbum(Long id) {
        return albumRepository.findById(id)
                .map(album -> {
                    albumRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}
