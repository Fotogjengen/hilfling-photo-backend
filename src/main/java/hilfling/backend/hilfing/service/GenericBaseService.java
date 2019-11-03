package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.BaseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenericBaseService<T extends BaseModel> {
    ResponseEntity<T> create(T entity);
    ResponseEntity<?> delete(Long id);
    ResponseEntity<T> getById(Long id);
    ResponseEntity<List<T>> getAll();
    ResponseEntity<T> update(T entity);
}
