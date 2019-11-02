package hilfling.backend.hilfing.repositories;

import hilfling.backend.hilfing.model.Gang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GangRepository extends JpaRepository<Gang, Long> {
}
