package onlinestore.paymentservice.event;

import lombok.Builder;
import lombok.Data;
import onlinestore.paymentservice.dto.OrderDetailDto;
import onlinestore.paymentservice.dto.OrderStatusDto;
import onlinestore.paymentservice.model.entity.OrderDataEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class OrderEvent implements Event {

    private static final String EVENT = "Order";

    private Long id;
    private String userName;
    private String clientName;
    private BigDecimal amount;
    private String destinationAddress;
    private String description;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private OrderStatusDto status;
    private List<OrderDetailDto> details;

    public static OrderEvent fromOrderDataEntity(OrderDataEntity orderData) {
        OrderEvent orderEvent = builder()
                .id(orderData.getOrderId())
                .userName(orderData.getUserName())
                .amount(orderData.getAmount())
                .destinationAddress(orderData.getDestinationAddress())
                .description(orderData.getDescription())
                .dateCreated(orderData.getDateCreated())
                .dateModified(orderData.getDateModified())
                .status(orderData.getStatus())
                .build();
        orderEvent.details = new ArrayList<>();
        orderData.getDetails().forEach(d -> orderEvent.details.add(OrderDetailDto.fromOrderDetailEntity(d)));
        return orderEvent;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }
}
