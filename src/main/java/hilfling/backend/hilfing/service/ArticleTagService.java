package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.ArticleTag;
import hilfling.backend.hilfing.repositories.ArticleTagRepository;
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
