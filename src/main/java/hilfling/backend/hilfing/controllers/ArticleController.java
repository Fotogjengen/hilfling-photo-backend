package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.Article;
import hilfling.backend.hilfing.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController extends GenericBaseControllerImplementation<Article> {
    @Autowired
    private ArticleService service;
    public ArticleService getService() {
        return service;
    }
}
