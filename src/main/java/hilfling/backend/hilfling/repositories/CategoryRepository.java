package hilfling.backend.hilfling.repositories;

import hilfling.backend.hilfling.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByTitle(String title);
}
