package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "place")
public class Place implements Serializable {
    public Place(String location) {
        this.location = location;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location", nullable = false, unique = true)
    private String location;
}
