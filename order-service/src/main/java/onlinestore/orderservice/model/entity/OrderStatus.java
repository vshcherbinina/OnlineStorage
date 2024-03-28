package onlinestore.orderservice.model.entity;

public enum OrderStatus {
    REGISTERED,
    PAID,
    PAYMENT_FAILED,
    INVENTED,
    INVENTION_FAILED,
    DELIVERED,
    DELIVERY_FAILED,
    UNEXPECTED_FAILURE
}
