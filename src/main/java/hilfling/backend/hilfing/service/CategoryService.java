package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Category;
import hilfling.backend.hilfing.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends GenericBaseServiceImplementation<Category> {
    @Autowired
    CategoryRepository repository;
    public CategoryRepository getRepository() {
        return repository;
    }

}
