package hilfling.backend.hilfling.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "security")
public class SecurityLevel implements Serializable, BaseModel {
    public SecurityLevel() {
    }

    public SecurityLevel(String type) {
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, unique = true)
    private String type;
}