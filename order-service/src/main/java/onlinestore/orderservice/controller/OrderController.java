package onlinestore.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import onlinestore.orderservice.dto.ErrorDto;
import onlinestore.orderservice.dto.OrderDto;
import onlinestore.orderservice.dto.StatusDto;
import onlinestore.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Slf4j
@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "List all orders in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order")
    public List<OrderDto> listOrders() {
        List<OrderDto> orderDtoList = orderService.getAllOrders();
        orderDtoList.sort(Comparator.comparing(OrderDto::getId));
        return orderDtoList;
    }

    @Operation(summary = "Get an order in system by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable @Parameter(description = "Id of order") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderById(id));
    }

    @Operation(summary = "Add order and start delivery process for it", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/order")
    public ResponseEntity<?> addOrder(@RequestBody OrderDto input) {
        orderService.checkData(input);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.addOrder(input));
    }

    @Operation(summary = "Update order status", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/order/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable @Parameter(description = "Id of order") long orderId,
                                                  @RequestBody StatusDto statusDto) {
        try {
            orderService.updateOrderStatus(orderId, statusDto);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Can't change status for order with id {}", orderId, ex);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .body(new ErrorDto(ex.getMessage()));
        }
    }
}
