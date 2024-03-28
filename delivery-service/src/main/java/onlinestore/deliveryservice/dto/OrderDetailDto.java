package onlinestore.deliveryservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailDto {
    private String productArticle;
    private Double price;
    private Double quantity;
    private Double amount;
}
