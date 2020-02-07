package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.BaseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenericBaseService<T extends BaseModel> {
    ResponseEntity<T> create(T entity);
    ResponseEntity<?> delete(Long id);
    ResponseEntity<T> getById(Long id);
    ResponseEntity<List<T>> getAll();
    ResponseEntity<T> update(T entity);
}
