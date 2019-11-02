package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "photo")
public class Article {
    public Article() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date_published", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "security_id", referencedColumnName = "id", nullable = false)
    private Security security;

    // TODO: photogangbanger column

}
