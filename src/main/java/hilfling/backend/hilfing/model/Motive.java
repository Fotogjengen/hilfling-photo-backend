package hilfling.backend.hilfing.model;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "motive")
public class Motive {
    public Motive() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "date_taken")
    private Date date_taken;

    // TODO: Autogenerate
    // @Temporal(TemporalType.DATE)
    @Column(name = "date_uploaded")
    private Date date_uploaded;

    @OneToOne
    // TODO: Always join?? What if we don't want to join?
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @Column(name = "category")
    private Category category;

    @OneToOne
    // TODO: Always join?? What if we don't want to join?
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @Column(name = "event_owner")
    @NotFound(action = NotFoundAction.IGNORE)
    private EventOwner eventOwner;
}
