package onlinestore.deliveryservice.event;

import lombok.Builder;
import lombok.Data;
import onlinestore.deliveryservice.dto.OrderDetailDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private String status;
    private String statusDescription;
    private Long orderId;
    List<OrderDetailDto> details;

    @Override
    public String getEvent() {
        return EVENT;
    }
}
