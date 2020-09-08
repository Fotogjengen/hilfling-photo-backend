package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "gang")
public class Gang extends BaseEntity<Long>{
    public Gang() {
    }

    public Gang(String name) {
        this.name = name;
    }

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
