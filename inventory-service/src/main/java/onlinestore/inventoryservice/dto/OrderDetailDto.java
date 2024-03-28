package onlinestore.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.inventoryservice.model.entity.InventoryDetailEntity;

@Data
@Builder
public class OrderDetailDto {
    private String productArticle;
    private Double price;
    private Double quantity;
    private Double amount;

public static OrderDetailDto fromInventoryDetailEntity (InventoryDetailEntity detail) {
    return builder()
            .productArticle(detail.getProductArticle())
            .price(detail.getPrice())
            .quantity(detail.getQuantity())
            .amount(detail.getAmount())
            .build();
}

}
