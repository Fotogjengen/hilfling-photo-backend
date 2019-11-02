package hilfling.backend.hilfing.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="album")
public class Album implements Serializable {
    public Album(String title, Boolean analog) {
        this.title = title;
        this.analog = analog;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    // TODO: Autogenerate
    @Column(name = "date_created", insertable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date dateCreated;

    @Column(name = "analog", nullable = false)
    @ColumnDefault("false")
    private Boolean analog;

}
