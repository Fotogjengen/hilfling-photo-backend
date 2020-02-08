package hilfling.backend.hilfling.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "photo")
@Inheritance(strategy = InheritanceType.JOINED)
public class Photo extends BasePhoto implements Serializable {
    public Photo() {
    }

    public Photo(
            Boolean goodPicture,
            Motive motive,
            Place place,
            SecurityLevel securityLevel,
            Gang gang,
            PhotoGangBanger photoGangBanger
    ) {
        super(goodPicture, motive, place, securityLevel, gang, photoGangBanger);
    }

    @Column(name = "s_url") // Is nullable when analog photo
    private String smallUrl;

    @Column(name = "m_url") // Is nullable when analog photo
    private String mediumUrl;

    @Column(name = "l_url") // Is nullable when analog photo
    private String largeUrl;

}
