package onlinestore.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.orderservice.model.entity.OrderStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StatusDto {
    private OrderStatus status;
    private LocalDateTime dateModified;
    private String statusDescription;
}
