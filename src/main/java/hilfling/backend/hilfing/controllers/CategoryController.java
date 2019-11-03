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
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping()
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category){
        return categoryService.createCategory(category);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category, @PathVariable("id") Long id) {
        return categoryService.updateCategory(category, id);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        return categoryService.deleteCategory(id);
    }
}
