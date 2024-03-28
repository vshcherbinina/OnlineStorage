package onlinestore.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.orderservice.model.entity.OrderStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {
    private OrderStatus status;
    private LocalDateTime dateModified;
    private String statusDescription;

    public StatusDto(OrderStatus status) {
        this.status = status;
        dateModified = LocalDateTime.now();
    }
}
