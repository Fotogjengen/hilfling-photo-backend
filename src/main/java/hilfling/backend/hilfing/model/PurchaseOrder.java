package hilfling.backend.hilfing.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder implements Serializable, BaseModel {
    public PurchaseOrder() {
    }

    public PurchaseOrder(
            String name,
            @Email String email,
            String address,
            String zipCode,
            String city,
            Boolean sendByPost,
            String comment,
            Date dateCreated,
            Boolean completed
    ) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.sendByPost = sendByPost;
        this.comment = comment;
        this.dateCreated = dateCreated;
        this.completed = completed;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "send_by_post", nullable = false)
    private Boolean sendByPost;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

    @Column(name = "completed", nullable = false)
    @ColumnDefault("false")
    private Boolean completed;

}
