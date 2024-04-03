package onlinestore.inventoryservice.service;

import onlinestore.inventoryservice.dto.InventoryDocumentDto;
import onlinestore.inventoryservice.dto.ProductDto;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;
import onlinestore.inventoryservice.model.entity.MovementEntity;
import onlinestore.inventoryservice.model.entity.ProductEntity;

import java.util.List;

public interface InventoryService {
    ProductEntity getProductByArticle(String article);

    void saveDocument(InventoryDocumentEntity document);

    void saveDocumentWithDetails(InventoryDocumentEntity document);

    ProductDto addProductStockBalance(ProductDto input);

    void changeProductStockBalance(MovementEntity movement);

    void loadDetails(InventoryDocumentEntity document);

    List<InventoryDocumentDto> findAllInventoryDocuments();

    InventoryDocumentDto getInventoryDocumentById(Long id);

    List<ProductEntity> findAllProducts();

    ProductEntity getProductById(Long id);

    void deductProductStockBalance(InventoryDocumentEntity document);

    void failedDocument(InventoryDocumentEntity document, String message);
}
