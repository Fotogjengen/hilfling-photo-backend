package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "photo_tag")
public class PhotoTag extends BaseEntity<Long>{
    public PhotoTag() {
    }

    public PhotoTag(String tag) {
        this.tag = tag;
    }

    @Column(name = "tag", nullable = false, unique = true)
    private String tag;
}
