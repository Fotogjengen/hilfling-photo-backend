package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Data
@Entity
@Table(name = "position")
public class Position implements Serializable {
    public Position() {
    }

    public Position(String title, @Email String email) {
        this.title = title;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

}
