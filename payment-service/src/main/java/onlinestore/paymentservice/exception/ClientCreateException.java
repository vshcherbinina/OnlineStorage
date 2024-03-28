package onlinestore.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientCreateException extends RuntimeException {
    public ClientCreateException() {
        super("clientCode is required");
    }

}
