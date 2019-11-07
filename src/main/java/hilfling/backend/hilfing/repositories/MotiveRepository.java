package hilfling.backend.hilfing.repositories;

import hilfling.backend.hilfing.model.Motive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotiveRepository extends JpaRepository<Motive, Long> {
}
