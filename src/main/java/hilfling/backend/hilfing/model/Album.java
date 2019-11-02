package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="album")
public class Album implements Serializable {
    public Album() {
    }

    public Album(String title, Date dateCreated, Boolean analog) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.analog = analog;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    // TODO: Autogenerate
    @Column(name = "date_created", updatable = false)
    private Date dateCreated;

    @Column(name = "analog")
    private Boolean analog;

}
