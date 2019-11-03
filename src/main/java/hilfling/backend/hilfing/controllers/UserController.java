package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.User;
import hilfling.backend.hilfing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends GenericBaseControllerImplementation<User> {
    @Autowired
    private UserService service;
    public UserService getService() {
        return service;
    }
}
