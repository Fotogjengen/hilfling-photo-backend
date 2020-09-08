package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "article_tag")
public class ArticleTag extends BaseEntity<Long> {
    public ArticleTag() {
    }

    public ArticleTag(String tag) {
        this.tag = tag;
    }

    @Column(name = "tag", nullable = false, unique = true)
    private String tag;
}
