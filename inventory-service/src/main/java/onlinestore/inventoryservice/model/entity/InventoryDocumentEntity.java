package onlinestore.inventoryservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
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
    @Column(columnDefinition = "numeric(15,2)")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;
    private String statusDescription;
    private String orderStatus;
    @Column(nullable = false)
    private Long orderId;

    @EqualsAndHashCode.Exclude
    @Transient
    private List<InventoryDetailEntity> details = new ArrayList<>();

    public void setStatusAndDateModification(DocumentStatus status) {
        this.status = status;
        this.dateModified = LocalDateTime.now();
    }
}
