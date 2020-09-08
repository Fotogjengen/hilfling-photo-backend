package hilfling.backend.hilfing.model

import lombok.Data
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "place")
class Place(
      @Column(name = "location", nullable = false, unique = true)
      val location: String

) : BaseEntity<Long>()
