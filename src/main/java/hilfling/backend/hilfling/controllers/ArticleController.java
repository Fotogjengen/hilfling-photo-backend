package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.Article;
import hilfling.backend.hilfling.service.ArticleService;
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
