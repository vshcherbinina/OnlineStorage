package onlinestore.deliveryservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDetailDto {
    private String productArticle;
    private BigDecimal price;
    private Double quantity;
    private BigDecimal amount;
}
