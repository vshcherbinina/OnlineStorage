package onlinestore.inventoryservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String article;
    @Column(columnDefinition = "numeric(15,2)")
    private BigDecimal price;
    @Column(nullable = false)
    private Double stockBalance;
    private String unit;
    private String description;
    private String stockAddress;
}
