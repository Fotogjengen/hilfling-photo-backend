package hilfling.backend.hilfling.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity(name = "analog_photos")
@Table(name = "analog_photos")
public class AnalogPhoto extends Photo {
    public AnalogPhoto() {
    }

    public AnalogPhoto(
            Boolean goodPicture,
            Motive motive,
            Place place,
            SecurityLevel securityLevel,
            Gang gang,
            PhotoGangBanger photoGangBanger,
            Integer imageNumber,
            Integer pageNumber
    ) {
        super(goodPicture, motive, place, securityLevel, gang, photoGangBanger);
        this.imageNumber = imageNumber;
        this.pageNumber = pageNumber;
    }

    @Column(name = "image_number", nullable = false)
    private Integer imageNumber;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;
}
