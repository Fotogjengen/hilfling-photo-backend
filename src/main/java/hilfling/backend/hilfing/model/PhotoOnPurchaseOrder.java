package hilfling.backend.hilfing.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "photo_on_purchase_order", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"purchase_order_id", "photo_id", "size"})
})
public class PhotoOnPurchaseOrder implements Serializable {
    public PhotoOnPurchaseOrder() {
    }

    public PhotoOnPurchaseOrder(PurchaseOrder purchaseOrder, Photo photo, String size, Integer amount) {
        this.purchaseOrder = purchaseOrder;
        this.photo = photo;
        this.size = size;
        this.amount = amount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "photo_id", referencedColumnName = "id", nullable = false)
    private Photo photo;

    @Column(name = "size", nullable = false)
    private String size;

    @Column(name = "amount", nullable = false)
    @ColumnDefault("1")
    private Integer amount;
}
