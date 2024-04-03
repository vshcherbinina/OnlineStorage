package onlinestore.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.orderservice.model.entity.OrderStatus;
import onlinestore.orderservice.model.entity.OrderStatusHistoryEntity;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderStatusHistoryDto {
    private Long id;
    private OrderStatus status;
    private LocalDateTime dateModified;
    private String statusDescription;

    public OrderStatusHistoryDto(OrderStatusHistoryEntity statusHistory) {
        this.setId(statusHistory.getId());
        this.setStatus(statusHistory.getStatus());
        this.setDateModified(statusHistory.getDateModified());
        this.setStatusDescription(statusHistory.getStatusDescription() == null? "" : statusHistory.getStatusDescription());
    }

}
