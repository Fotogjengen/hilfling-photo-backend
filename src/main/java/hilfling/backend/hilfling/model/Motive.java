package hilfling.backend.hilfling.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "motive", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "date_taken"})
})
public class Motive implements Serializable, BaseModel {
    public Motive() {
    }

    public Motive(String title, Date date_taken, Category category, EventOwner eventOwner) {
        this.title = title;
        this.date_taken = date_taken;
        this.category = category;
        this.eventOwner = eventOwner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "date_taken", nullable = false)
    private Date date_taken;

    @OneToOne // TODO: ManyToOne?
    // TODO: Always join?? What if we don't want to join?
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @OneToOne // TODO: ManyToOne?
    // TODO: Always join?? What if we don't want to join?
    @JoinColumn(name = "event_owner_id", referencedColumnName = "id")
    private EventOwner eventOwner;
}
