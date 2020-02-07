package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.ArticleTag;
import hilfling.backend.hilfling.repositories.ArticleTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagService extends
        GenericBaseServiceImplementation<ArticleTag> {
    @Autowired
    ArticleTagRepository repository;
    public ArticleTagRepository getRepository() {
        return repository;
    }

}
