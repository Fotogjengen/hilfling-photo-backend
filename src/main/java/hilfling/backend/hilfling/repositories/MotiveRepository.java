package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.Motive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotiveRepository extends JpaRepository<Motive, Long> {
}
