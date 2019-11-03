package hilfling.backend.hilfing.repositories;

import hilfling.backend.hilfing.model.PhotographyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotographyRequestRepository extends JpaRepository<PhotographyRequest, Long> {
}
