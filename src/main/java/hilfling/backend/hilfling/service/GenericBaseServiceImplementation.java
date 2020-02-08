package hilfling.backend.hilfling.service;

import hilfling.backend.hilfling.model.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
public abstract class GenericBaseServiceImplementation<T extends BaseModel> implements GenericBaseService<T> {
    abstract JpaRepository<T, Long> getRepository();


    @Override
    public ResponseEntity<T> create(T entity) {
        if (entity.getId() != null) {
            return ResponseEntity.status(404).build();
        }
        try {
            getRepository().save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception error) {
            return ResponseEntity.status(304).build();
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
