package onlinestore.inventoryservice.model.util;

import lombok.Getter;
import onlinestore.inventoryservice.model.entity.InventoryDetailEntity;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;
import onlinestore.inventoryservice.model.entity.ProductEntity;
import onlinestore.inventoryservice.model.repository.InventoryDetailRepository;
import onlinestore.inventoryservice.model.repository.InventoryDocumentRepository;
import onlinestore.inventoryservice.model.repository.MovementRepository;
import onlinestore.inventoryservice.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Getter
public class RepositoryUtil {

    private final ProductRepository productRepository;

    private final MovementRepository movementRepository;

    private final InventoryDocumentRepository documentRepository;

    private final InventoryDetailRepository detailRepository;

    @Autowired
    public RepositoryUtil(ProductRepository productRepository, MovementRepository movementRepository,
                          InventoryDocumentRepository documentRepository, InventoryDetailRepository detailRepository) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.documentRepository = documentRepository;
        this.detailRepository = detailRepository;
    }

    public void saveProduct(ProductEntity product) {
        ProductEntity savedProduct = productRepository.save(product);
        product.setId(savedProduct.getId());
    }

    public List<InventoryDocumentEntity> findAll() {
        return documentRepository.findAll();
    }

    public List<InventoryDetailEntity> findAllDetailsByDocumentId(Long id) {
        return detailRepository.findAllByDocumentId(id);
    }

    public Optional<InventoryDocumentEntity> findById(Long id) {
        return documentRepository.findById(id);
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductEntity> getProductById(Long id) {
        return productRepository.findById(id);
    }
}

