package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "article_tag")
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
