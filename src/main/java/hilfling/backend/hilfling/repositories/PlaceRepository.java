package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
