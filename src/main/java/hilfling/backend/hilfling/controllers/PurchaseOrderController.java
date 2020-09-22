package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.PurchaseOrder;
import hilfling.backend.hilfling.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase_orders")
public class PurchaseOrderController extends GenericBaseControllerImplementation<PurchaseOrder> {
    @Autowired
    private PurchaseOrderService service;
    public PurchaseOrderService getService() {
        return service;
    }
}
