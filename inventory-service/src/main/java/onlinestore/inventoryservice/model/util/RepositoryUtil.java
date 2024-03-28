package onlinestore.inventoryservice.model.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.inventoryservice.model.entity.ProductEntity;
import onlinestore.inventoryservice.model.repository.InventoryDetailRepository;
import onlinestore.inventoryservice.model.repository.InventoryDocumentRepository;
import onlinestore.inventoryservice.model.repository.MovementRepository;
import onlinestore.inventoryservice.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
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
}

