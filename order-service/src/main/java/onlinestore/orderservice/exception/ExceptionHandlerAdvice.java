package onlinestore.orderservice.exception;

import lombok.extern.slf4j.Slf4j;
import onlinestore.orderservice.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> exceptionHandler(Exception ex) {
        HttpStatus status = HttpStatus.EXPECTATION_FAILED;
        if (ex instanceof IncorrectInputException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof OrderNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }
        log.error("Error: {}", ex.getMessage());
        Arrays.stream(ex.getStackTrace()).toList().forEach(str -> log.trace(str.toString()));
        return ResponseEntity
                .status(status)
                .body(new ErrorDto(ex.getMessage()));
    }


}
