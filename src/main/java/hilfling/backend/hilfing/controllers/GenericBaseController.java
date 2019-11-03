package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.service.GenericBaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface GenericBaseController<T> {
    ResponseEntity<T> create(T entity);
    ResponseEntity<T> getById(Long id);
    ResponseEntity<T> delete(Long id);
    ResponseEntity<T> update(T entity);
    ResponseEntity<T> getAll();
}
