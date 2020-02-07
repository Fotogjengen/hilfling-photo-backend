package hilfling.backend.hilfling.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "photo_tag")
public class PhotoTag implements Serializable, BaseModel {
    public PhotoTag() {
    }

    public PhotoTag(String tag) {
        this.tag = tag;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag", nullable = false, unique = true)
    private String tag;
}
