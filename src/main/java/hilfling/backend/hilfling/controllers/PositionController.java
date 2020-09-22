package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.Position;
import hilfling.backend.hilfling.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/positions")
public class PositionController extends GenericBaseControllerImplementation<Position> {
    @Autowired
    private PositionService service;
    public PositionService getService() {
        return service;
    }
}
