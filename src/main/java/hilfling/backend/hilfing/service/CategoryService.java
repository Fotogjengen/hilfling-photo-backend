package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Category;
import hilfling.backend.hilfing.repositories.CategoryRepository;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<Category> createCategory(Category category) {
        try {
            return ResponseEntity.ok().body(categoryRepository.save(category));

        } catch (Exception e) {
            // TODO: make logger
            return ResponseEntity.status(304).build();
        }
    }

    public ResponseEntity<Category> updateCategory (Category category, Long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        category.setId(id);
        return ResponseEntity.ok().body(categoryRepository.save(category));
    }

    public ResponseEntity deleteCategory (Long id) {
        try {
            categoryRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}
