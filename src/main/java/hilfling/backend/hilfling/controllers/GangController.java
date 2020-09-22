package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.Gang;
import hilfling.backend.hilfling.service.GangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gangs")
public class GangController extends GenericBaseControllerImplementation<Gang> {
    @Autowired
    private GangService service;
    public GangService getService() {
        return service;
    }

}
