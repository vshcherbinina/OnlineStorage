package onlinestore.orderservice.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "order_detail")
@Data
@NoArgsConstructor
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "order_detail_to_order_fk"), nullable = false)
    private OrderEntity order;

    private String productArticle;

    private Double price;

    private Double quantity;

    private Double amount;

    public OrderDetailEntity withOrder(OrderEntity order) {
        this.order = order;
        return this;
    }

    public OrderDetailEntity withProductArticle(String productArticle) {
        this.productArticle = productArticle;
        return this;
    }

    public OrderDetailEntity withPrice(Double price) {
        this.price = price;
        return this;
    }

    public OrderDetailEntity withQuantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderDetailEntity withAmount(Double amount) {
        this.amount = amount;
        return this;
    }
}
