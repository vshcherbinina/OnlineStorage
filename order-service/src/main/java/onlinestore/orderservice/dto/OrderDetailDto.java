package onlinestore.orderservice.dto;

import lombok.Builder;
import lombok.Data;
import onlinestore.orderservice.model.entity.OrderDetailEntity;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@Builder
public class OrderDetailDto {
    @NotBlank
    private String productArticle;
    private BigDecimal price;
    @NotBlank
    private Double quantity;
    private BigDecimal amount;

    public OrderDetailEntity toOrderDetail() {
        return new OrderDetailEntity()
                .withProductArticle(productArticle)
                .withPrice(price)
                .withQuantity(quantity)
                .withAmount(amount);
    }

    public static OrderDetailDto fromOrderDetail(OrderDetailEntity orderDetail) {
        return builder()
                .productArticle(orderDetail.getProductArticle())
                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .amount(orderDetail.getAmount())
                .build();
    }

}
