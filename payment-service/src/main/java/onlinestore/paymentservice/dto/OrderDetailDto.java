package onlinestore.paymentservice.dto;

import lombok.Builder;
import lombok.Data;
import onlinestore.paymentservice.model.entity.OrderDetailEntity;

@Data
@Builder
public class OrderDetailDto {
    private String productArticle;
    private Double price;
    private Double quantity;
    private Double amount;

    public static OrderDetailDto fromOrderDetailEntity(OrderDetailEntity detailEntity) {
        return builder()
                .productArticle(detailEntity.getProductArticle())
                .price(detailEntity.getPrice())
                .quantity(detailEntity.getQuantity())
                .amount(detailEntity.getAmount())
                .build();
    }
}
