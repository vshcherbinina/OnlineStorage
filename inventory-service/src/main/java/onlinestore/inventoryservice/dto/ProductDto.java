package onlinestore.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import onlinestore.inventoryservice.model.entity.ProductEntity;

@Data
@Builder
public class ProductDto {
    private Long id;
    private String article;
    private Double price;
    private Double quantity;
    private String unit;
    private String description;
    private String stockAddress;

    public static ProductDto fromProductEntity(ProductEntity productEntity){
        return builder()
                .id(productEntity.getId())
                .article(productEntity.getArticle())
                .price(productEntity.getPrice())
                .quantity(productEntity.getStockBalance())
                .unit(productEntity.getUnit() == null ? "" : productEntity.getUnit())
                .description(productEntity.getDescription() == null ? "" : productEntity.getDescription())
                .stockAddress(productEntity.getStockAddress() == null ? "" : productEntity.getStockAddress())
                .build();
    }

    public ProductEntity toProductEntity() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setArticle(this.article);
        productEntity.setPrice(this.price);
        productEntity.setStockBalance(this.quantity);
        productEntity.setUnit(this.unit);
        productEntity.setDescription(this.description);
        productEntity.setStockAddress(this.stockAddress);
        return productEntity;
    }
}
