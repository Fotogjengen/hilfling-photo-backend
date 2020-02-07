package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.Motive;
import hilfling.backend.hilfling.service.MotiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/motives")
public class MotiveController extends GenericBaseControllerImplementation<Motive> {
    @Autowired
    private MotiveService service;
    public MotiveService getService() {
        return service;
    }
}
