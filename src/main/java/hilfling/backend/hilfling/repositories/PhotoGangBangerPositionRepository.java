package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.PhotoGangBangerPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoGangBangerPositionRepository extends JpaRepository<PhotoGangBangerPosition, Long> {
}
