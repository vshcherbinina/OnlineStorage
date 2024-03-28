package onlinestore.paymentservice.dto;

import lombok.Builder;
import lombok.Data;
import onlinestore.paymentservice.model.entity.PaymentEntity;

@Data
@Builder
public class PaymentDto {
    private Long id;
    private String currency;
    private Double amount;
    private String dateCreated;
    private String dateModified;
    private String status;
    private String statusDescription;
    private Long orderId;

    public static PaymentDto fromPaymentEntity(PaymentEntity payment) {
        return builder()
                .id(payment.getId())
                .currency(payment.getCurrency())
                .amount(payment.getAmount())
                .dateCreated(payment.getDateCreated().toString())
                .dateModified(payment.getDateModified().toString())
                .status(payment.getStatus().toString())
                .statusDescription(payment.getStatusDescription() == null ? "" : payment.getStatusDescription())
                .orderId(payment.getOrderId())
                .build();
    }
}
