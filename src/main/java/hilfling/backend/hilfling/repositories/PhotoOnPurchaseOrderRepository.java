package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.PhotoOnPurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoOnPurchaseOrderRepository extends JpaRepository<PhotoOnPurchaseOrder, Long> {
}
