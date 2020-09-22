package hilfling.backend.hilfling.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity(name = "articles")
@Table(name = "articles")
public class Article implements Serializable, BaseModel {
    public Article() {
    }

    public Article(String title, String content, SecurityLevel securityLevel, PhotoGangBanger photoGangBanger) {
        this.title = title;
        this.content = content;
        this.securityLevel = securityLevel;
        this.photoGangBanger = photoGangBanger;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "date_published", insertable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date datePublished;

    @ManyToOne
    @JoinColumn(name = "security_level_id", referencedColumnName = "id", nullable = false)
    private SecurityLevel securityLevel;

    @ManyToOne
    @JoinColumn(name = "photo_gang_banger_id", referencedColumnName = "id", nullable = false)
    private PhotoGangBanger photoGangBanger;

}
