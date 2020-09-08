package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "security")
public class SecurityLevel extends BaseEntity<Long>{
    public SecurityLevel() {
    }

    public SecurityLevel(String type) {
        this.type = type;
    }

    @Column(name = "type", nullable = false, unique = true)
    private String type;
}
