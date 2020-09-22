package hilfling.backend.hilfling.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "event_owners")
@Table(name = "event_owners")
public class EventOwner implements Serializable, BaseModel {
    public EventOwner() {
    }

    public EventOwner(String owner) {
        this.owner = owner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner", nullable = false, unique = true)
    private String owner;
}
