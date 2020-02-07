package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.Category;
import hilfling.backend.hilfling.repositories.CategoryRepository;
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
