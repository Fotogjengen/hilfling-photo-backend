package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "event_owner")
public class EventOwner {
    public EventOwner(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner")
    private String owner;
}
