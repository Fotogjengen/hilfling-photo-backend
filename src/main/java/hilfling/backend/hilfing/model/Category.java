package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="category")
public class Category {
    public Category() {

    }
    public Category(String title) {
        this.title = title;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

}
