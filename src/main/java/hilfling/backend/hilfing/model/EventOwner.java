package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "event_owner")
public class EventOwner extends BaseEntity<Long>{
    public EventOwner() {
    }

    public EventOwner(String owner) {
        this.owner = owner;
    }

    @Column(name = "owner", nullable = false, unique = true)
    private String owner;
}
