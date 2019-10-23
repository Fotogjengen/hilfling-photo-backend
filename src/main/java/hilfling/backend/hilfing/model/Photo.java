package hilfling.backend.hilfing.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "photo")
public class Photo {
    private Long id;
    private String motive;

    public Photo() {

    };
    public Photo(String motive, Long id) {
        this.motive = motive;
        this.id = id;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
            return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
    public void setMotive(String motive) { this.motive = motive;}

    @Column(name = "motive", nullable = false)
    public  String getMotive() {
        return this.motive;
    }

    @Override
    public String toString() {
        return "Photo" + this.motive;
    }
}
