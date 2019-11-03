package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.User;
import hilfling.backend.hilfing.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends
        GenericBaseServiceImplementation<User> {

    @Autowired
    UserRepository repository;
    public UserRepository getRepository() {
        return repository;
    }
}
