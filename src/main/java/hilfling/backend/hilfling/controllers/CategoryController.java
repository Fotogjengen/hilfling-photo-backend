package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.Category;
import hilfling.backend.hilfling.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController extends GenericBaseControllerImplementation<Category>{
    @Autowired
    private CategoryService service;
    public CategoryService getService() {
        return service;
    }
}
