package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "event_owner")
public class EventOwner implements Serializable {
    public EventOwner(String owner) {
        this.owner = owner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner", nullable = false, unique = true)
    private String owner;
}
