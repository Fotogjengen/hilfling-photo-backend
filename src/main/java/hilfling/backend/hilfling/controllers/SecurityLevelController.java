package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.SecurityLevel;
import hilfling.backend.hilfling.service.SecurityLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/security_levels")
public class SecurityLevelController extends GenericBaseControllerImplementation<SecurityLevel> {

    @Autowired
    private SecurityLevelService service;
    public SecurityLevelService  getService() {
        return service;
    }
}
