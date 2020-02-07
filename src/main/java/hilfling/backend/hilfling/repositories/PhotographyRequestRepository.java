package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.PhotographyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotographyRequestRepository extends JpaRepository<PhotographyRequest, Long> {
}
