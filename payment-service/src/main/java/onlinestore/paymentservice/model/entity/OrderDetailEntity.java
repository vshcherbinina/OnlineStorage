package onlinestore.paymentservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import onlinestore.paymentservice.dto.OrderDetailDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity(name = "order_detail")
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude @ToString.Exclude @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_data_id", foreignKey = @ForeignKey(name = "order_detail_to_order_data_fk"), nullable = false)
    private OrderDataEntity orderData;

    private String productArticle;
    @Column(columnDefinition = "numeric(15,2)")
    private BigDecimal price;
    private Double quantity;
    @Column(columnDefinition = "numeric(15,2)")
    private BigDecimal amount;

    public static OrderDetailEntity fromOrderDetailDto(OrderDetailDto detailDto) {
        OrderDetailEntity detail = new OrderDetailEntity();
        detail.productArticle = detailDto.getProductArticle();
        detail.price = detailDto.getPrice();
        detail.quantity = detailDto.getQuantity();
        detail.amount = detailDto.getAmount();
        return detail;
    }

}
