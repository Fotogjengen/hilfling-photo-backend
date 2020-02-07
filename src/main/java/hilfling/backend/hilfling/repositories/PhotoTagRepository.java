package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.PhotoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoTagRepository extends JpaRepository<PhotoTag, Long> {
}
