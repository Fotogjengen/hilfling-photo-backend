package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="album")
public class Album {
    public Album() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    // TODO: Autogenerate
    @Column(name = "date_created", updatable = false)
    private Date date_created;

    @Column(name = "analog")
    private Boolean analog;

}
