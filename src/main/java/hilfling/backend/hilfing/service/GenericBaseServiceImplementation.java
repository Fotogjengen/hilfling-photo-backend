package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.BaseEntity;
import hilfling.backend.hilfing.model.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
public abstract class GenericBaseServiceImplementation<T extends BaseEntity<Long>> implements GenericBaseService<T> {
    abstract JpaRepository<T, Long> getRepository();
    @Override
    public ResponseEntity<T> create(T entity) {
        if (entity.getId() != null) {
            // TODO: Implement
            return ResponseEntity.status(403).build();
        }
        getRepository().save(entity);
        return new ResponseEntity(entity, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        getRepository().deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<T> getById(Long id) {
        return ResponseEntity.ok().body(getRepository().getOne(id));
    }

    @Override
    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok().body(getRepository().findAll());
    }

    @Override
    public ResponseEntity update(T entity) {
        if(getRepository().findById(entity.getId()).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        getRepository().save(entity);
        return ResponseEntity.ok().build();
    }

}
