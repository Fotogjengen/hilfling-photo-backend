package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.PhotoOnPurchaseOrder;
import hilfling.backend.hilfing.service.PhotoOnPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/photo_on_purchase_orders")
public class PhotoOnPurchaseOrderController extends GenericBaseControllerImplementation<PhotoOnPurchaseOrder> {
    @Autowired
    private PhotoOnPurchaseOrderService service;
    public PhotoOnPurchaseOrderService getService() {
        return service;
    }
}
