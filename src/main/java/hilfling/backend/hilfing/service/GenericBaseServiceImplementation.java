package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
public abstract class GenericBaseServiceImplementation<T extends BaseModel> implements GenericBaseService<T> {
    abstract JpaRepository<T, Long> getRepository();
    @Override
    public ResponseEntity<T> create(T entity) {
        if (entity.getId() != null) {
            return ResponseEntity.status(403).build();
        }
        try {
            getRepository().save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception error) {
            // TODO: Lage exceptions. Ikke sikkert å sende errors fra backend
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
       return getRepository().findById(id)
       .map(entity -> {
           getRepository().deleteById(id);
           return ResponseEntity.ok().build();
       }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<T> getById(Long id) {
        return getRepository().findById(id)
                .map(entity -> ResponseEntity.ok().body(entity))
                .orElse(ResponseEntity.notFound().build());
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
