package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.ArticleTag;
import hilfling.backend.hilfing.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article_tags")
public class ArticleTagController extends GenericBaseControllerImplementation<ArticleTag> {
    @Autowired
    private ArticleTagService service;
    public ArticleTagService getService() {
        return service;
    }

    @Override
    public ResponseEntity<ArticleTag> getAll() {
        return super.getAll();
    }
}
