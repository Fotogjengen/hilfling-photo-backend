package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.Gang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GangRepository extends JpaRepository<Gang, Long> {
}
