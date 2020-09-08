package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "security_levels")
@Table(name = "security_levels")
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
