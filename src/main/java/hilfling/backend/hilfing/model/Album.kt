package hilfling.backend.hilfing.model

import lombok.Data
import org.hibernate.annotations.ColumnDefault
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "album", uniqueConstraints = [UniqueConstraint(columnNames = ["title", "analog"])])
class Album (
        @Column(name = "title", nullable = false)
        var title: String,
        @Column(name = "date_created", insertable = false, updatable = false)
        @ColumnDefault("CURRENT_TIMESTAMP")
        val dateCreated: Date,
        @Column(name = "analog")
        @ColumnDefault("false")
        private var analog: Boolean = false

): BaseEntity<Long>()