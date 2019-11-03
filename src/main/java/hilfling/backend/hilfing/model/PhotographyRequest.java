package hilfling.backend.hilfing.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name = "photography_request")
public class PhotographyRequest implements Serializable, BaseModel {
    public PhotographyRequest() {
    }

    public PhotographyRequest(
            Date startTime,
            Date endTime,
            String place,
            Boolean intern,
            String type,
            String name,
            @Email String email,
            String phone,
            String description
    ) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.intern = intern;
        this.type = type;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "place", nullable = false)
    private String place;

    @Column(name = "intern", nullable = false)
    @ColumnDefault("false")
    private Boolean intern;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

}
