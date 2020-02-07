package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.User;
import hilfling.backend.hilfling.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends GenericBaseControllerImplementation<User> {
    @Autowired
    private UserService service;
    public UserService getService() {
        return service;
    }
}
