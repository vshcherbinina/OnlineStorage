package onlinestore.paymentservice.exception;

import onlinestore.paymentservice.dto.ErrorDto;
import onlinestore.paymentservice.exception.ClientAlreadyExistException;
import onlinestore.paymentservice.exception.ClientNotFoundException;
import onlinestore.paymentservice.exception.PaymentNotFoundException;
import onlinestore.paymentservice.exception.TransactionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    @ExceptionHandler
    public ResponseEntity<ErrorDto> exceptionHandler(Exception ex) {
        HttpStatus status = HttpStatus.EXPECTATION_FAILED;
        if ((ex instanceof ClientAlreadyExistException) ||
            (ex instanceof ClientCreateException)
        ) {
            status = HttpStatus.BAD_REQUEST;
        } else if (
                (ex instanceof ClientNotFoundException) ||
                (ex instanceof PaymentNotFoundException) ||
                (ex instanceof TransactionNotFoundException)
        ) {
            status = HttpStatus.NOT_FOUND;
        }
        logger.error("Error: {}", ex.getMessage());
        Arrays.stream(ex.getStackTrace()).toList().forEach(str -> logger.trace(str.toString()));
        return ResponseEntity
                .status(status)
                .body(new ErrorDto(ex.getMessage()));
    }

}
