package hilfling.backend.hilfing.repositories;

import hilfling.backend.hilfing.model.PhotoGangBanger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoGangBangerRepository extends JpaRepository<PhotoGangBanger, Long> {
}
