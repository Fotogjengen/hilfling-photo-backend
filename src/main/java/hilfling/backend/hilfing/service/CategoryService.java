package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Category;
import hilfling.backend.hilfing.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        if (categoryRepository.findByTitle(category.getTitle()) == null) {
            return categoryRepository.save(category);
        }
        else {
            return categoryRepository.findByTitle(category.getTitle());
        }

    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

}
