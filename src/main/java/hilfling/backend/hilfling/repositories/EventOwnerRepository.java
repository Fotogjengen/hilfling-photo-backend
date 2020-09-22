package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.EventOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventOwnerRepository extends JpaRepository<EventOwner, Long> {
}
