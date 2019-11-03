package hilfling.backend.hilfing.repositories;

import hilfling.backend.hilfing.model.PhotoOnPurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoOnPurchaseOrderRepository extends JpaRepository<PhotoOnPurchaseOrder, Long> {
}
