package onlinestore.paymentservice.dto;

import lombok.Builder;
import lombok.Data;
import onlinestore.paymentservice.model.entity.OrderDetailEntity;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDetailDto {
    private String productArticle;
    private BigDecimal price;
    private Double quantity;
    private BigDecimal amount;

    public static OrderDetailDto fromOrderDetailEntity(OrderDetailEntity detailEntity) {
        return builder()
                .productArticle(detailEntity.getProductArticle())
                .price(detailEntity.getPrice())
                .quantity(detailEntity.getQuantity())
                .amount(detailEntity.getAmount())
                .build();
    }
}
