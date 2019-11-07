package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.PhotoOnPurchaseOrder;
import hilfling.backend.hilfing.repositories.PhotoOnPurchaseOrderRepository;
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
