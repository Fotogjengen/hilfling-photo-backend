package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.Gang;
import hilfling.backend.hilfing.service.GangService;
import hilfling.backend.hilfing.service.GenericBaseServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gangs")
public class GangController extends GenericBaseControllerImplementation<Gang> {
    @Autowired
    private GangService service;
    public GangService getService() {
        return service;
    }

}
