package onlinestore.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;
import onlinestore.inventoryservice.model.entity.InventoryDetailEntity;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDetailDto {
    private String productArticle;
    private BigDecimal price;
    private Double quantity;
    private BigDecimal amount;

public static OrderDetailDto fromInventoryDetailEntity (InventoryDetailEntity detail) {
    return builder()
            .productArticle(detail.getProductArticle())
            .price(detail.getPrice())
            .quantity(detail.getQuantity())
            .amount(detail.getAmount())
            .build();
}

    public OrderDetailDto(String productArticle, BigDecimal price, Double quantity, BigDecimal amount) {
        this.productArticle = productArticle;
        this.price = price;
        this.quantity = quantity;
        this.amount = amount;
    }

}
