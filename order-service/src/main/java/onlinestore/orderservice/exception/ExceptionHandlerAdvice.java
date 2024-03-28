package onlinestore.orderservice.exception;

import onlinestore.orderservice.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    @ExceptionHandler
    public ResponseEntity<ErrorDto> exceptionHandler(Exception ex) {
        HttpStatus status = HttpStatus.EXPECTATION_FAILED;
        if (ex instanceof IncorrectInputException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof OrderNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }
        logger.error("Error: {}", ex.getMessage());
        Arrays.stream(ex.getStackTrace()).toList().forEach(str -> logger.trace(str.toString()));
        return ResponseEntity
                .status(status)
                .body(new ErrorDto(ex.getMessage()));
    }


}
