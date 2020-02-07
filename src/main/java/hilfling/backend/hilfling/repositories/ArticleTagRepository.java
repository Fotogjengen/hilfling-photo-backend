package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
}
