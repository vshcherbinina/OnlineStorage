package onlinestore.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import onlinestore.orderservice.dto.OrderDetailDto;
import onlinestore.orderservice.model.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class OrderEvent implements Event {

    private static final String EVENT = "Order";

    private Long id;
    private String userName;
    private Double amount;
    private String destinationAddress;
    private String description;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private String status;
    private List<OrderDetailDto> details;

    public static OrderEvent fromOrder(OrderEntity order) {
        OrderEvent orderCreatedEvent = builder()
                .id(order.getId())
                .userName(order.getUserName())
                .amount(order.getAmount())
                .destinationAddress(order.getDestinationAddress())
                .description(order.getDescription())
                .dateCreated(order.getDateCreated())
                .dateModified(order.getDateModified())
                .status(order.getStatus().toString())
                .build();
        orderCreatedEvent.details = new ArrayList<>();
        order.getDetails().forEach(detail -> orderCreatedEvent.details.add(OrderDetailDto.fromOrderDetail(detail)));
        return orderCreatedEvent;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }
}
