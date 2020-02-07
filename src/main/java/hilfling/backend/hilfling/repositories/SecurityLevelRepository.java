package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.SecurityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityLevelRepository extends JpaRepository<SecurityLevel, Long> {
}
