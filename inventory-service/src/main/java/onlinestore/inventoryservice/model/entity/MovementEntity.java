package onlinestore.inventoryservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "movement")
public class MovementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "movement_to_product_fk"))
    private ProductEntity product;

    @Column(nullable = false)
    private int income;
    @Column(nullable = false)
    private LocalDateTime date;
    @Column(nullable = false)
    private Double quantity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_document_id", foreignKey = @ForeignKey(name = "movement_to_document_fk"))
    private InventoryDocumentEntity document;

    public MovementEntity(ProductEntity product, Double quantity, int income) {
        this.product = product;
        this.income = income;
        this.date = LocalDateTime.now();
        this.quantity = quantity;
    }

    public MovementEntity(InventoryDetailEntity detail, int income) {
        this.product = detail.getProduct();
        this.income = income;
        this.date = LocalDateTime.now();
        this.quantity = detail.getQuantity();
        this.document = detail.getDocument();
    }
}
