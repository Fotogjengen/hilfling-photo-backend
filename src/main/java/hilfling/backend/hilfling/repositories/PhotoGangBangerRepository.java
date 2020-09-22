package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.PhotoGangBanger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoGangBangerRepository extends JpaRepository<PhotoGangBanger, Long> {
}
