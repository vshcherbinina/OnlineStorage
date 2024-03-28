package onlinestore.orderservice.dto;

import lombok.Builder;
import lombok.Data;
import onlinestore.orderservice.exception.OrderNotFoundException;
import onlinestore.orderservice.model.entity.OrderDetailEntity;
import onlinestore.orderservice.model.entity.OrderEntity;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class OrderDto {
    private Long id;
    private String dateCreated;
    @NotBlank
    private String userName;
    @NotBlank
    private Double amount;
    private String destinationAddress;
    private String description;
    private String status;
    private List<OrderDetailDto> details;

    public OrderEntity toOrder() {
        OrderEntity order = new OrderEntity()
                .withUserName(userName)
                .withAmount(amount)
                .withDestinationAddress(destinationAddress)
                .withDescription(description);
        details.forEach(detailDto -> {
            OrderDetailEntity detail = detailDto.toOrderDetail();
            detail.setOrder(order);
            order.getDetails().add(detail);
        });
        return order;
    }

    public static OrderDto fromOrder(OrderEntity order) throws IllegalArgumentException{
        if (order == null) {
            throw new IllegalArgumentException("Order is null");
        }
        OrderDto orderDto = builder()
                .id(order.getId())
                .dateCreated(order.getDateCreated().toString())
                .userName(order.getUserName())
                .amount(order.getAmount())
                .destinationAddress(order.getDestinationAddress())
                .description(order.getDescription())
                .status(order.getStatus().toString())
                .build();
        orderDto.details = new ArrayList<>();
        order.getDetails().forEach(detail -> orderDto.details.add(OrderDetailDto.fromOrderDetail(detail)));
        return orderDto;
    }
}
