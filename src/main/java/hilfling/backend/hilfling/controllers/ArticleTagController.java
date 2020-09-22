package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.ArticleTag;
import hilfling.backend.hilfling.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article_tags")
public class ArticleTagController extends GenericBaseControllerImplementation<ArticleTag> {
    @Autowired
    private ArticleTagService service;
    public ArticleTagService getService() {
        return service;
    }
}
