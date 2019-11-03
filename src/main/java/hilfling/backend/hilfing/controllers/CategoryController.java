package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.Category;
import hilfling.backend.hilfing.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController extends GenericBaseControllerImplementation<Category>{
    @Autowired
    private CategoryService service;
    public CategoryService getService() {
        return service;
    }
}
