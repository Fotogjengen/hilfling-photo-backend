package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.Category;
import hilfling.backend.hilfing.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public List<Category> geCategories() {
        return categoryService.getCategories();
    }

    @PostMapping()
    public Category createCategory(@Valid @RequestBody Category category){
        return categoryService.createCategory(category);
    }
}
