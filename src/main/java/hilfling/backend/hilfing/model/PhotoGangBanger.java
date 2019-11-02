package hilfling.backend.hilfing.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Data
@Entity
@Table(name = "photo_gang_banger")
public class PhotoGangBanger extends User {

    public PhotoGangBanger(
            String firstName,
            String lastName,
            String username,
            @Email String email,
            @URL String profilePicture,
            String phoneNumber,
            String sex,
            String relationshipStatus,
            String semesterStart,
            Boolean active,
            Boolean pang,
            String address,
            String zipCode,
            String city
    ) {
        super(firstName, lastName, username, email, profilePicture, phoneNumber, sex);
        this.relationshipStatus = relationshipStatus;
        this.semesterStart = semesterStart;
        this.active = active;
        this.pang = pang;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
    }

    @Column(name = "relationship_status", nullable = false)
    private String relationshipStatus;

    @Column(name = "semester_start", nullable = false)
    private String semesterStart;

    @Column(name = "active", nullable = false)
    @ColumnDefault("true")
    private Boolean active;

    @Column(name = "pang", nullable = false)
    @ColumnDefault("false")
    private Boolean pang;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "city", nullable = false)
    private String city;
}