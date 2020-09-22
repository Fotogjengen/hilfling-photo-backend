package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.Article;
import hilfling.backend.hilfling.repositories.ArticleRepository;
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
