package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Category;
import hilfling.backend.hilfing.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends GenericBaseServiceImplementation<Category> {
    @Autowired
    CategoryRepository repository;

    public CategoryRepository getRepository() {
        return repository;
    }

    @Override
    public ResponseEntity<Category> create(Category category) {
        // if category with that title exists return 403 status kode
        if (repository.findByTitle(category.getTitle()) != null ) {
            return ResponseEntity.status(403).build();
        }
        try {
            // else create album
            Category savedCategory = repository.save(category);
            return ResponseEntity.ok().body(savedCategory);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    };

}
