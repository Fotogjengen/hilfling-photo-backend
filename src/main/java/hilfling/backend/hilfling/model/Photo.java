package hilfling.backend.hilfling.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "photo")
@Inheritance(strategy = InheritanceType.JOINED)
public class Photo implements Serializable, BaseModel {
    public Photo() {
    }

    public Photo(
            String smallUrl,
            String mediumUrl,
            String largeUrl,
            Boolean goodPicture,
            Motive motive,
            Place place,
            SecurityLevel securityLevel,
            Gang gang,
            PhotoGangBanger photoGangBanger
    ) {
        this.smallUrl = smallUrl;
        this.mediumUrl = mediumUrl;
        this.largeUrl = largeUrl;
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

    @Column(name = "s_url") // Is nullable when analog photo
    private String smallUrl;

    @Column(name = "m_url") // Is nullable when analog photo
    private String mediumUrl;

    @Column(name = "l_url") // Is nullable when analog photo
    private String largeUrl;

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
