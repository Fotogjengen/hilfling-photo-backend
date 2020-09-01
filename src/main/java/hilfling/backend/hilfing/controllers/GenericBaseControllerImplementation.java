package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.BaseModel;
import hilfling.backend.hilfing.service.GenericBaseServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public abstract class GenericBaseControllerImplementation<T extends BaseModel> implements GenericBaseController<T> {
    abstract GenericBaseServiceImplementation getService();

    @Override
    @PostMapping
    public ResponseEntity<T> create(@Valid @RequestBody T entity) {
       return getService().create(entity);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable("id") Long id) {
        return getService().getById(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<T> delete(@PathVariable("id") Long id) {
        return getService().delete(id);
    }

    @Override
    @PatchMapping
    public ResponseEntity<T> update(@Valid @RequestBody T entity) {
        return getService().update(entity);
    }

    @Override
    @GetMapping
    public ResponseEntity<T> getAll() {
        return getService().getAll();
    }
}
