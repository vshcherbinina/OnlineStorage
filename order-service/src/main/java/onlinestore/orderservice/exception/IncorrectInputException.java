package onlinestore.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectInputException extends RuntimeException {
    public IncorrectInputException(String dtlError) {
        super("Data entered incorrectly: " + dtlError);
    }



}
