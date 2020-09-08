package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.BaseEntity;
import hilfling.backend.hilfing.model.BaseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenericBaseService<T extends BaseEntity<Long>> {
    ResponseEntity<T> create(T entity);
    ResponseEntity<?> delete(Long id);
    ResponseEntity<T> getById(Long id);
    ResponseEntity<List<T>> getAll();
    ResponseEntity<T> update(T entity);
}
