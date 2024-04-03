package onlinestore.inventoryservice;

import onlinestore.inventoryservice.dto.OrderDetailDto;
import onlinestore.inventoryservice.dto.ProductDto;
import onlinestore.inventoryservice.event.OrderEvent;
import onlinestore.inventoryservice.model.entity.InventoryDetailEntity;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;
import onlinestore.inventoryservice.model.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class TestUtil {
    public static ProductDto getInstanceProductDto() {
        return ProductDto.builder()
                .article("Cream")
                .price(BigDecimal.valueOf(50))
                .quantity(5D)
                .stockAddress("Moscow, Lenina st., 1")
                .description("test")
                .unit("pc")
                .build();
    }

    public static ProductEntity getInstanceProduct() {
        return getInstanceProductDto().toProductEntity();
    }

    public static OrderEvent getInstanceOrderEvent() {
        OrderEvent orderEvent = OrderEvent.builder()
                .id(1L)
                .userName("first")
                .clientName("Client First")
                .amount(BigDecimal.valueOf(100))
                .destinationAddress("Saint Petersburg, Nevsky Avenue, 1")
                .description("test Petrov")
                .build();
        ProductEntity product = getInstanceProduct();
        OrderDetailDto detail = OrderDetailDto.builder()
                .productArticle(product.getArticle())
                .price(product.getPrice())
                .quantity(1D)
                .amount(BigDecimal.valueOf(50))
                .build();
        orderEvent.setDetails(new ArrayList<>());
        orderEvent.getDetails().add(detail);
        return orderEvent;
    }

    public static InventoryDocumentEntity getInstanceDocument() {
        OrderEvent orderEvent = getInstanceOrderEvent();
        InventoryDocumentEntity document = orderEvent.toInventoryDocumentEntity();
        ProductEntity product = getInstanceProduct();
        document.getDetails().forEach(d -> d.setProduct(product));
        return document;
    }

    public static InventoryDetailEntity getDetailFromDocument(InventoryDocumentEntity document) {
        if (document.getDetails().stream().findFirst().isPresent()) {
            return document.getDetails().stream().findFirst().orElse(null);
        }
        return null;
    }

    public static ProductEntity getProductFromDocument(InventoryDocumentEntity document) {
        return Optional.of(Objects.requireNonNull(getDetailFromDocument(document)).getProduct())
                .orElse(getInstanceProduct());
    }

}
