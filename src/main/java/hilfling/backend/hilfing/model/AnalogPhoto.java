package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "analog_photo")
public class AnalogPhoto extends Photo {

    public AnalogPhoto(
            String smallUrl,
            String mediumUrl,
            String largeUrl,
            Boolean goodPicture,
            Motive motive,
            Place place,
            Security security,
            Gang gang,
            PhotoGangBanger photoGangBanger,
            Integer imageNumber,
            Integer pageNumber
    ) {
        super(smallUrl, mediumUrl, largeUrl, goodPicture, motive, place, security, gang, photoGangBanger);
        this.imageNumber = imageNumber;
        this.pageNumber = pageNumber;
    }

    @Column(name = "image_number", nullable = false)
    private Integer imageNumber;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;
}
