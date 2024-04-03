package onlinestore.inventoryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import onlinestore.inventoryservice.dto.InventoryDocumentDto;
import onlinestore.inventoryservice.dto.ProductDto;
import onlinestore.inventoryservice.model.entity.ProductEntity;
import onlinestore.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class InventoryController {
    public final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "List all inventory documents", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/inventory")
    public List<InventoryDocumentDto> listInventoryDocuments() {
        return inventoryService.findAllInventoryDocuments();
    }

    @Operation(summary = "Get inventory document by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/inventory/{id}")
    public InventoryDocumentDto getInventoryDocument(@PathVariable @Parameter(description = "inventory document id") Long id) {
        return inventoryService.getInventoryDocumentById(id);
    }

    @Operation(summary = "List all products", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/product")
    public List<ProductEntity> listProducts() {
        return inventoryService.findAllProducts();
    }

    @Operation(summary = "Get product by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/product/{id}")
    public ProductDto getProduct(@PathVariable @Parameter(description = "inventory document id") Long id) {
        return Optional.of(inventoryService.getProductById(id))
                .map(ProductDto::fromProductEntity)
                .orElse(null);
    }

    @Operation(summary = "Create new product or add quantity to stock balance, if product exist ", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto input) {
        ProductDto productDto = inventoryService.addProductStockBalance(input);
        return ResponseEntity
            .status(input.getId() == null ? HttpStatus.CREATED : HttpStatus.OK)
            .body(productDto);
    }
}
