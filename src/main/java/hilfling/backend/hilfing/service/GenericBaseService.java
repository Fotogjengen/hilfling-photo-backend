package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenericBaseService<T extends BaseModel> {
    ResponseEntity<T> create(T entity);
    ResponseEntity<?> delete(Long id);
    ResponseEntity<T> getById(Long id);
    ResponseEntity<Page<T>> getAll(Pageable pageable);
    ResponseEntity<T> update(T entity);
}
