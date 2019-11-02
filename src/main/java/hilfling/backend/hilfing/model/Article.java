package hilfling.backend.hilfing.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "photo")
public class Article {
    public Article() {
    }

    public Article(String title, String content, Security security) {
        this.title = title;
        this.content = content;
        this.security = security;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date_published", insertable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date datePublished;

    @ManyToOne
    @JoinColumn(name = "security_id", referencedColumnName = "id", nullable = false)
    private Security security;

    // TODO: photogangbanger column

}
