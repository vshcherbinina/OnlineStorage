package onlinestore.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientAlreadyExistException extends RuntimeException {
    public ClientAlreadyExistException(String userName) {
        super("Client with user name '" + userName + "' already exist");
    }
}
