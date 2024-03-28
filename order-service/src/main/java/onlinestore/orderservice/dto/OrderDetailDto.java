package onlinestore.orderservice.dto;

import lombok.Builder;
import lombok.Data;
import onlinestore.orderservice.model.entity.OrderDetailEntity;
import onlinestore.orderservice.model.entity.OrderEntity;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class OrderDetailDto {
    @NotBlank
    private String productArticle;
    private Double price;
    @NotBlank
    private Double quantity;
    private Double amount;

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
