package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.User;
import hilfling.backend.hilfling.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends
        GenericBaseServiceImplementation<User> {

    @Autowired
    UserRepository repository;
    public UserRepository getRepository() {
        return repository;
    }
}
