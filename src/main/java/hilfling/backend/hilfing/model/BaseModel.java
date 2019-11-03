package hilfling.backend.hilfing.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public interface BaseModel {
 void setId(Long id);
}
