package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.PurchaseOrder;
import hilfling.backend.hilfing.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderService extends
        GenericBaseServiceImplementation<PurchaseOrder> {
    @Autowired
    PurchaseOrderRepository repository;
    public PurchaseOrderRepository getRepository() {
        return repository;
    }

}
