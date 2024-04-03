package onlinestore.inventoryservice.service;

import onlinestore.inventoryservice.dto.InventoryDocumentDto;
import onlinestore.inventoryservice.dto.ProductDto;
import onlinestore.inventoryservice.exception.ProductCreateException;
import onlinestore.inventoryservice.exception.ProductNotFoundException;
import onlinestore.inventoryservice.model.entity.*;
import onlinestore.inventoryservice.model.util.LockByKey;
import onlinestore.inventoryservice.model.util.RepositoryUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService{

    private final RepositoryUtil repositoryUtil;

    public InventoryServiceImpl(RepositoryUtil repositoryUtil, LockByKey lockByProductId) {
        this.repositoryUtil = repositoryUtil;
        this.lockByProductId = lockByProductId;
    }

    private final LockByKey lockByProductId;

    @Override
    public ProductEntity getProductByArticle(String article) throws ProductNotFoundException {
        return repositoryUtil.getProductRepository().findOneByArticle(article)
                .orElseThrow(() -> new ProductNotFoundException(article));
    }

    @Override
    public void saveDocument(InventoryDocumentEntity document) {
        InventoryDocumentEntity savedDocument = repositoryUtil.getDocumentRepository().save(document);
        document.setId(savedDocument.getId());
    }

    @Override
    public void saveDocumentWithDetails(InventoryDocumentEntity document) {
        saveDocument(document);
        document.getDetails().forEach(d -> {
            if (d.getProduct() == null) {
                ProductEntity product = repositoryUtil.getProductRepository().findOneByArticle(d.getProductArticle())
                        .orElseThrow(() -> new ProductNotFoundException(d.getProductArticle()));
                d.setProduct(product);
            }
            repositoryUtil.getDetailRepository().save(d);
        });
    }

    private void deleteMovementsByDocumentId(Long documentId) {
        repositoryUtil.getMovementRepository().deleteAllByDocumentId(documentId);
    }

    private void saveMovement(MovementEntity movement) {
        MovementEntity savedMovement = repositoryUtil.getMovementRepository().save(movement);
        movement.setId(savedMovement.getId());
    }

    public ProductEntity findProduct(ProductDto input) {
        if (input.getArticle() == null || input.getArticle().isBlank()) {
            throw new ProductCreateException();
        }
        if (input.getQuantity() == null || input.getQuantity().compareTo(0D) <= 0) {
            throw new ProductCreateException(-1D);
        }
        return repositoryUtil.getProductRepository().findOneByArticle(input.getArticle())
                .orElse(input.toProductEntity());

    }

    @Override
    @Transactional
    public ProductDto addProductStockBalance(ProductDto input) {
        ProductEntity product = findProduct(input);
        if (product.getId() == null) {
            product.setStockBalance(0D);
            repositoryUtil.saveProduct(product);
        } else {
            input.setId(product.getId());
        }
        if (input.getQuantity().compareTo(0D) > 0) {
            createMovementAndChangeBalance(product, input.getQuantity(), true);
        }
        return ProductDto.fromProductEntity(product);
    }

    private void createMovementAndChangeBalance(ProductEntity product, Double quantity, boolean income) {
        MovementEntity movement = new MovementEntity(product, quantity, income ? 1 : -1);
        changeProductStockBalance(movement);
    }

    @Override
    @Transactional
    public void changeProductStockBalance(MovementEntity movement) {
        ProductEntity product = movement.getProduct();
        try {
            lockByProductId.lock(product.getId());
            Double changedBalance = product.getStockBalance() +
                    movement.getIncome() * movement.getQuantity();
            product.setStockBalance(changedBalance);
            saveMovement(movement);
        } finally {
            lockByProductId.unlock(product.getId());
        }
    }

    @Override
    public void loadDetails(InventoryDocumentEntity document) {
        List<InventoryDetailEntity> details = repositoryUtil.findAllDetailsByDocumentId(document.getId());
        details.forEach(detail -> detail.setDocument(document));
        document.setDetails(details);

    }

    @Override
    public List<InventoryDocumentDto> findAllInventoryDocuments() {
        List<InventoryDocumentEntity> documentEntityList = repositoryUtil.findAll();
        documentEntityList.forEach(this::loadDetails);
        List<InventoryDocumentDto> documentDtoList = new ArrayList<>();
        documentEntityList.forEach(documentEntity -> documentDtoList.add(InventoryDocumentDto.fromInventoryDocumentEntity(documentEntity)));
        documentDtoList.sort(Comparator.comparing(InventoryDocumentDto::getId));
        return documentDtoList;
    }

    @Override
    public InventoryDocumentDto getInventoryDocumentById(Long id) {
        InventoryDocumentEntity documentEntity = repositoryUtil.findById(id)
                .orElse(null);
        if (documentEntity == null) {
            return null;
        }
        loadDetails(documentEntity);
        return InventoryDocumentDto.fromInventoryDocumentEntity(documentEntity);
    }

    @Override
    public List<ProductEntity> findAllProducts() {
        return repositoryUtil.getAllProducts();
    }

    @Override
    public ProductEntity getProductById(Long id) {
        return repositoryUtil.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private void updateDocumentStatus(InventoryDocumentEntity document, DocumentStatus status) {
        document.setStatusAndDateModification(status);
        saveDocument(document);
    }

    @Override
    public void failedDocument(InventoryDocumentEntity document, String textError) {
        document.setStatusDescription(textError);
        updateDocumentStatus(document, DocumentStatus.INVENTION_FAILED);
        deleteMovementsByDocumentId(document.getId());
    }

    private boolean checkDetail(InventoryDetailEntity detail) {
        try {
            ProductEntity product = getProductByArticle(detail.getProductArticle());
            detail.setProduct(product);
        } catch (Exception e) {
            failedDocument(detail.getDocument(), e.getMessage());
            return false;
        };
        if (detail.getProduct().getStockBalance().compareTo(detail.getQuantity()) < 0)  {
            failedDocument(detail.getDocument(), "Insufficient quantity of product with article " + detail.getProductArticle());
            return false;
        }
        return true;
    }

    @Override
    public void deductProductStockBalance(InventoryDocumentEntity document) {
        try {
            lockByProductId.lockAllForDocument(document);
            boolean checkResult = true;
            for (InventoryDetailEntity detail : document.getDetails()) {
                checkResult = checkDetail(detail);
                if (!checkResult) {
                    break;
                }
            }
            if (checkResult) {
                for (InventoryDetailEntity detail : document.getDetails()) {
                    MovementEntity movement = new MovementEntity(detail, -1);
                    saveMovement(movement);
                    changeProductStockBalance(movement);
                }
                updateDocumentStatus(document, DocumentStatus.INVENTED);
            }
        } finally {
            lockByProductId.unlockAllForDocument(document);
        }
    }

}
