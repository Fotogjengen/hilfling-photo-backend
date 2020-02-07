package hilfling.backend.hilfling.model;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Data
@Entity
@Table(name = "hilfling_user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User implements Serializable, BaseModel {
    public User() {
    }

    public User(
            String firstName,
            String lastName,
            String username,
            @Email String email,
            @URL String profilePicture,
            String phoneNumber,
            String sex
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @URL
    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "sex", nullable = false)
    private String sex;
}
