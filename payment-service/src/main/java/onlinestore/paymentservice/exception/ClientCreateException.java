package onlinestore.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientCreateException extends RuntimeException {
    public ClientCreateException() {
        super("'userName' is required");
    }

    public ClientCreateException(String userName) {
        super("user named '" + userName + "' not found - need to register user first");
    }
}
