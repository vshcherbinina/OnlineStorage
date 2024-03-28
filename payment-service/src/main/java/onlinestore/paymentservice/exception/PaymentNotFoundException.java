package onlinestore.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long id) {
        super(String.format("Payment with id=%s not found", id));
    }

    public PaymentNotFoundException(Long orderId, String description) {
        super(String.format("Payment with order id=%s not found - error when processing message status change (%s)", orderId, description));
    }

}
