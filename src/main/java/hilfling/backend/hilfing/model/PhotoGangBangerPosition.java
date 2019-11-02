package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "photo_gang_banger_position"  , uniqueConstraints = {
        @UniqueConstraint(columnNames = {"photo_gang_banger_id", "position_id"})
})
public class PhotoGangBangerPosition implements Serializable {
    public PhotoGangBangerPosition() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photo_gang_banger_id", referencedColumnName = "id", nullable = false)
    private PhotoGangBanger photoGangBanger;

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id", nullable = false)
    private Position position;

    @Column(name = "current", nullable = false)
    private Boolean current;
}
