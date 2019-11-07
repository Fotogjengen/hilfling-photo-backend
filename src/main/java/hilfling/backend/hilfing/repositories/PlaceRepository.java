package hilfling.backend.hilfing.repositories;

import hilfling.backend.hilfing.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
