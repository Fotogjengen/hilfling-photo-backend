package hilfling.backend.hilfling.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "article_tags")
@Table(name = "article_tags")
public class ArticleTag implements Serializable, BaseModel {
    public ArticleTag() {
    }

    public ArticleTag(String tag) {
        this.tag = tag;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag", nullable = false, unique = true)
    private String tag;
}
