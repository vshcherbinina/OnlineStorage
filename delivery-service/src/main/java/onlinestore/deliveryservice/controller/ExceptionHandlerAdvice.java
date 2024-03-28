package onlinestore.deliveryservice.controller;

import onlinestore.deliveryservice.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> exceptionHandler(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ErrorDto(ex.getMessage()));
    }

}
