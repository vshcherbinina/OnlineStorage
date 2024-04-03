package onlinestore.inventoryservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import onlinestore.inventoryservice.dto.OrderDetailDto;
import onlinestore.inventoryservice.model.entity.DocumentStatus;
import onlinestore.inventoryservice.model.entity.InventoryDetailEntity;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private String status;
    private List<OrderDetailDto> details;

    public InventoryDocumentEntity toInventoryDocumentEntity() {
        InventoryDocumentEntity document = new InventoryDocumentEntity();
        document.setUserName(this.getUserName());
        document.setClientName(this.getClientName());
        document.setDestinationAddress(this.getDestinationAddress());
        document.setDescription(this.getDescription());
        document.setIncome(-1);
        document.setOrderDataCreated(this.getDateCreated());
        document.setAmount(this.getAmount());
        document.setOrderStatus(this.getStatus());
        document.setOrderId(this.getId());
        document.setStatusAndDateModification(DocumentStatus.CREATED);
        document.setDateCreated(this.dateModified);
        this.getDetails().forEach(d -> document.getDetails().add(new InventoryDetailEntity(d)));
        document.getDetails().forEach(d -> d.setDocument(document));
        return document;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }
}
