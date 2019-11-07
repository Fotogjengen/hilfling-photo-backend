package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.Motive;
import hilfling.backend.hilfing.service.MotiveService;
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
