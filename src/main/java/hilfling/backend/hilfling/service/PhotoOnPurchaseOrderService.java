package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.PhotoOnPurchaseOrder;
import hilfling.backend.hilfling.repositories.PhotoOnPurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoOnPurchaseOrderService extends
        GenericBaseServiceImplementation<PhotoOnPurchaseOrder> {
    @Autowired
    PhotoOnPurchaseOrderRepository repository;
    public PhotoOnPurchaseOrderRepository getRepository() {
        return repository;
    }

}
