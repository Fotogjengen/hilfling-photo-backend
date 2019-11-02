package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.User;
import hilfling.backend.hilfing.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<User> createUser(User user) {
        try {
            User newUser = userRepository.save(user);
            return ResponseEntity.ok().body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(304).build();
        }
    }

    public ResponseEntity<User> getUser(Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    public ResponseEntity<User> updateUser(User userDetails, Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(userDetails.getEmail());
                    user.setFirstName(userDetails.getFirstName());
                    user.setLastName(userDetails.getLastName());
                    user.setPhoneNumber(userDetails.getPhoneNumber());
                    user.setProfilePicture(userDetails.getProfilePicture());
                    user.setSex(userDetails.getSex());
                    user.setUsername(userDetails.getUsername());
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok().body(updatedUser);
                }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }


}
