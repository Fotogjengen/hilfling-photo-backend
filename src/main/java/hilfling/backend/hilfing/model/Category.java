package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="category")
public class Category extends BaseEntity<Long>{
    public Category() {
    }

    public Category(String title) {
        this.title = title;
    }

    @Column(name = "title", nullable = false, unique = true)
    private String title;

}
