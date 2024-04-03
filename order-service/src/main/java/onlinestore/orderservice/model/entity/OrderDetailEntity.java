package onlinestore.orderservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "order_detail")
@Data
@NoArgsConstructor
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude @EqualsAndHashCode.Exclude @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "order_detail_to_order_fk"), nullable = false)
    private OrderEntity order;

    @Column(nullable = false)
    private String productArticle;
    @Column(columnDefinition = "numeric(15,2)")
    private BigDecimal price;
    @Column(nullable = false)
    private Double quantity;
    @Column(columnDefinition = "numeric(15,2)", nullable = false)
    private BigDecimal amount;

    public OrderDetailEntity withOrder(OrderEntity order) {
        this.order = order;
        return this;
    }

    public OrderDetailEntity withProductArticle(String productArticle) {
        this.productArticle = productArticle;
        return this;
    }

    public OrderDetailEntity withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public OrderDetailEntity withQuantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderDetailEntity withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

}
