package onlinestore.inventoryservice.event;

import lombok.*;
import onlinestore.inventoryservice.dto.OrderDetailDto;
import onlinestore.inventoryservice.model.entity.DocumentStatus;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class InventoryDocumentEvent implements Event {

    private static final String EVENT = "InventoryDocument";

    private Long id;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private String userName;
    private String clientName;
    private String destinationAddress;
    private String description;
    private int income;
    private LocalDateTime orderDataCreated;
    private BigDecimal amount;
    private DocumentStatus status;
    private String statusDescription;
    private Long orderId;
    List<OrderDetailDto> details;

    public static InventoryDocumentEvent fromInventoryDocumentEntity(InventoryDocumentEntity documentEntity) {
        InventoryDocumentEvent documentEvent = builder()
                .dateCreated(documentEntity.getDateCreated())
                .dateModified(documentEntity.getDateModified())
                .userName(documentEntity.getUserName())
                .clientName(documentEntity.getClientName())
                .destinationAddress(documentEntity.getDestinationAddress())
                .description(documentEntity.getDescription())
                .income(documentEntity.getIncome())
                .orderDataCreated(documentEntity.getOrderDataCreated())
                .amount(documentEntity.getAmount())
                .status(documentEntity.getStatus())
                .statusDescription(documentEntity.getStatusDescription())
                .orderId(documentEntity.getOrderId())
                .build();
        documentEvent.details = new ArrayList<>();
        documentEntity.getDetails().forEach(detail -> documentEvent.details.add(OrderDetailDto.fromInventoryDetailEntity(detail)));
        return documentEvent;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }
}
