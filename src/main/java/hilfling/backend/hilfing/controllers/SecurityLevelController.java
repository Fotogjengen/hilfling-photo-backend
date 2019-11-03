package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.SecurityLevel;
import hilfling.backend.hilfing.service.GenericBaseServiceImplementation;
import hilfling.backend.hilfing.service.SecurityLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Security;
import java.util.List;

@RestController
@RequestMapping("/api/v1/security_levels")
public class SecurityLevelController extends GenericBaseControllerImplementation<SecurityLevel> {

    @Autowired
    private SecurityLevelService service;
    public SecurityLevelService  getService() {
        return service;
    }
}
