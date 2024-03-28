package onlinestore.inventoryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_MODIFIED)
public class ProductCreateException extends RuntimeException {
    public ProductCreateException() {
        super("article is required");
    }

    public ProductCreateException(Double quantity) {
        super("the quantity must be positive");
    }

}
