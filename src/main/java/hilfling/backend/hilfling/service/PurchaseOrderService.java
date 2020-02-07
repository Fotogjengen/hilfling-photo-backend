package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.PurchaseOrder;
import hilfling.backend.hilfling.repositories.PurchaseOrderRepository;
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
