package onlinestore.paymentservice.event;

import lombok.Builder;
import lombok.Data;
import onlinestore.paymentservice.dto.OrderStatusDto;
import onlinestore.paymentservice.model.entity.PaymentEntity;
import onlinestore.paymentservice.model.entity.PaymentStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderStatusEvent implements Event {

    private static final String EVENT = "OrderStatus";

    private Long orderId;
    private LocalDateTime dateModified;
    private OrderStatusDto status;
    private String statusDescription;

    public static OrderStatusEvent  fromPaymentEntity(PaymentEntity payment) {
        return builder()
                .orderId(payment.getOrderId())
                .status(payment.getStatus().equals(PaymentStatus.APPROVED) ?
                        OrderStatusDto.PAID :
                        OrderStatusDto.PAYMENT_FAILED)
                .dateModified(payment.getDateModified())
                .statusDescription(payment.getStatusDescription())
                .build();
    }

    @Override
    public String getEvent() {
        return EVENT;
    }
}
