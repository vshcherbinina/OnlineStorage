package onlinestore.deliveryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import onlinestore.deliveryservice.model.entity.DeliveryEntity;
import onlinestore.deliveryservice.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class DeliveryController {
    public final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Operation(summary = "List all deliveries", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/delivery")
    public List<DeliveryEntity> listDeliveries() {
        return deliveryService.findAllDeliveries();
    }

    @Operation(summary = "Get delivery by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/delivery/{id}")
    public DeliveryEntity getDelivery(@PathVariable @Parameter(description = "delivery id") Long id) {
        return deliveryService.getDeliveryById(id).orElse(null);
    }
}
