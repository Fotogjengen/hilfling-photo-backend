package hilfling.backend.hilfing.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "photo_gang_banger_positions")
@Table(name = "photo_gang_banger_positions"  , uniqueConstraints = {
        @UniqueConstraint(columnNames = {"photo_gang_banger_id", "position_id"})
})
public class PhotoGangBangerPosition implements Serializable, BaseModel {
    public PhotoGangBangerPosition() {
    }

    public PhotoGangBangerPosition(PhotoGangBanger photoGangBanger, Position position, Boolean current) {
        this.photoGangBanger = photoGangBanger;
        this.position = position;
        this.current = current;
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
    @ColumnDefault("true")
    private Boolean current;
}
