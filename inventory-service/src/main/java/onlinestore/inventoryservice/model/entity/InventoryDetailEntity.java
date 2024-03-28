package onlinestore.inventoryservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.inventoryservice.dto.OrderDetailDto;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "inventory_detail")
public class InventoryDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", foreignKey = @ForeignKey(name = "inventory_detail_to_document_data_fk"))
    private InventoryDocumentEntity document;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "movement_to_product_fk"))
    private ProductEntity product;
    private String productArticle;

    @Column(nullable = false)
    private Double quantity;

    private Double price;
    private Double amount;

    public InventoryDetailEntity(OrderDetailDto detailDto) {
        this.productArticle = detailDto.getProductArticle();
        this.price = detailDto.getPrice();
        this.quantity = detailDto.getQuantity();
        this.amount = detailDto.getAmount();
    }
}
