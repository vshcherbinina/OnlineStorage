package onlinestore.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;
import onlinestore.inventoryservice.model.entity.DocumentStatus;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class InventoryDocumentDto {
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

    public static InventoryDocumentDto fromInventoryDocumentEntity(InventoryDocumentEntity documentEntity) {
        InventoryDocumentDto documentDto = builder()
                .id(documentEntity.getId())
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
        documentDto.details = new ArrayList<>();
        documentEntity.getDetails().forEach(detail -> documentDto.details.add(OrderDetailDto.fromInventoryDetailEntity(detail)));
        return documentDto;
    }

}
