package hilfling.backend.hilfing.repositories;

import hilfling.backend.hilfing.model.SecurityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityLevelRepository extends JpaRepository<SecurityLevel, Long> {
}
