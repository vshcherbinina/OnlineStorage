package onlinestore.paymentservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import onlinestore.paymentservice.dto.OrderDetailDto;

import javax.persistence.*;

@Data
@ToString
@NoArgsConstructor
@Entity(name = "order_detail")
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_data_id", foreignKey = @ForeignKey(name = "order_detail_to_order_data_fk"), nullable = false)
    private OrderDataEntity orderData;

    private String productArticle;
    private Double price;
    private Double quantity;
    private Double amount;

    public static OrderDetailEntity fromOrderDetailDto(OrderDetailDto detailDto) {
        OrderDetailEntity detail = new OrderDetailEntity();
        detail.productArticle = detailDto.getProductArticle();
        detail.price = detailDto.getPrice();
        detail.quantity = detailDto.getQuantity();
        detail.amount = detailDto.getAmount();
        return detail;
    }

}
