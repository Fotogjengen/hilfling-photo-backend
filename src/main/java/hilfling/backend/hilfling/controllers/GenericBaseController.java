package hilfling.backend.hilfling.controllers;

import org.springframework.http.ResponseEntity;

public interface GenericBaseController<T> {
    ResponseEntity<T> create(T entity);
    ResponseEntity<T> getById(Long id);
    ResponseEntity<T> delete(Long id);
    ResponseEntity<T> update(T entity);
    ResponseEntity<T> getAll();
}
