package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Article;
import hilfling.backend.hilfing.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService extends
        GenericBaseServiceImplementation<Article> {
    @Autowired
    ArticleRepository repository;
    public ArticleRepository getRepository() {
        return repository;
    }

}
