package hilfling.backend.hilfing.repositories;

import hilfling.backend.hilfing.model.EventOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventOwnerRepository extends JpaRepository<EventOwner, Long> {
}