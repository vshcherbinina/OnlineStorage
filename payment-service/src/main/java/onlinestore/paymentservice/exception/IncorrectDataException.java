package onlinestore.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class IncorrectDataException extends RuntimeException {
    public IncorrectDataException(String objectNotFound, Long id) {
        super("Incorrect data: " + objectNotFound + " with id or client id =" + id + " not found");
    }

}
