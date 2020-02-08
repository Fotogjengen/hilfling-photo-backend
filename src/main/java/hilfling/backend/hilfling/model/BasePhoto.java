package hilfling.backend.hilfling.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BasePhoto implements BaseModel {
    public BasePhoto() {}

    public BasePhoto(Boolean goodPicture, Motive motive, Place place, SecurityLevel securityLevel, Gang gang, PhotoGangBanger photoGangBanger) {
        this.goodPicture = goodPicture;
        this.motive = motive;
        this.place = place;
        this.securityLevel = securityLevel;
        this.gang = gang;
        this.photoGangBanger = photoGangBanger;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "good_picture", nullable = false)
    @ColumnDefault("false")
    private Boolean goodPicture;

    @ManyToOne
    @JoinColumn(name = "motive_id", referencedColumnName = "id", nullable = false)
    private Motive motive;

    @ManyToOne
    @JoinColumn(name = "place_id", referencedColumnName = "id", nullable = false)
    private Place place;

    @ManyToOne
    @JoinColumn(name = "security_id", referencedColumnName = "id", nullable = false)
    private SecurityLevel securityLevel;

    @ManyToOne
    @JoinColumn(name = "gang_id", referencedColumnName = "id")
    private Gang gang;

    @ManyToOne
    @JoinColumn(name = "photo_gang_banger_id", referencedColumnName = "id", nullable = false)
    private PhotoGangBanger photoGangBanger;
}
