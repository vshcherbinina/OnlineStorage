package onlinestore.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String userName) {
        super("Client for user with name \"" + userName + "\" not found");
    }

    public ClientNotFoundException(Long id) {
        super("Client with id=" + id + " not found");
    }
}
