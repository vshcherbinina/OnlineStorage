package onlinestore.inventoryservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.inventoryservice.event.OrderEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "inventory_document")
public class InventoryDocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @Column(nullable = false)
    private LocalDateTime dateModified;

    @Column(nullable = false)
    private String userName;
    private String clientName;

    @Column(nullable = false)
    private String destinationAddress;
    private String description;

    @Column(nullable = false)
    private int income;
    private LocalDateTime orderDataCreated;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;
    private String statusDescription;
    private String orderStatus;

    @Column(nullable = false)
    private Long orderId;
    @Transient
    List<InventoryDetailEntity> details;

    public InventoryDocumentEntity(OrderEvent orderEvent) {
        this.userName = orderEvent.getUserName();
        this.clientName = orderEvent.getClientName();
        this.destinationAddress = orderEvent.getDestinationAddress();
        this.description = orderEvent.getDescription();
        this.income = -1;
        this.orderDataCreated = orderEvent.getDateCreated();
        this.amount = orderEvent.getAmount();
        this.orderStatus = orderEvent.getStatus();
        this.orderId = orderEvent.getId();
        setStatusAndDateModification(DocumentStatus.CREATED);
        this.dateCreated = this.dateModified;
        details = new ArrayList<>();
        orderEvent.getDetails().forEach(d -> details.add(new InventoryDetailEntity(d)));
        details.forEach(d -> d.setDocument(this));
    }

    public void setStatusAndDateModification(DocumentStatus status) {
        this.status = status;
        this.dateModified = LocalDateTime.now();
    }

}
