package onlinestore.inventoryservice.event;

import lombok.Builder;
import lombok.Data;
import onlinestore.inventoryservice.dto.OrderDetailDto;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;

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
    private Double amount;
    private String destinationAddress;
    private String description;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private String status;
    private List<OrderDetailDto> details;

    public static OrderEvent fromInventoryDocumentEntity(InventoryDocumentEntity document) {
        OrderEvent orderEvent = builder()
                .id(document.getOrderId())
                .userName(document.getUserName())
                .clientName(document.getClientName())
                .amount(document.getAmount())
                .destinationAddress(document.getDestinationAddress())
                .description(document.getDescription())
                .dateCreated(document.getOrderDataCreated())
                .dateModified(document.getDateModified())
                .status(document.getStatus().toString())
                .build();
        orderEvent.details = new ArrayList<>();
        document.getDetails().forEach(d -> orderEvent.getDetails().add(OrderDetailDto.fromInventoryDetailEntity(d)));
        return orderEvent;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }
}
