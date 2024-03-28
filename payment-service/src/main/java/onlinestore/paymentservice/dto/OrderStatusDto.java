package onlinestore.paymentservice.dto;

public enum OrderStatusDto {
    REGISTERED,
    PAID,
    PAYMENT_FAILED,
    INVENTED,
    INVENTION_FAILED,
    DELIVERED,
    DELIVERY_FAILED,
    UNEXPECTED_FAILURE
}
