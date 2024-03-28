package onlinestore.inventoryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String article) {
        super("Product with article \"" + article + "\" not found");
    }

    public ProductNotFoundException(Long id) {
        super("Product with id=" + id + " not found");
    }
}
