package onlinestore.paymentservice.exception;

import lombok.extern.slf4j.Slf4j;
import onlinestore.paymentservice.dto.ErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(ExceptionHandlerAdvice.class)
public class ExceptionHandlerAdviceTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ExceptionHandlerAdvice exceptionHandlerAdvice;

    @Configuration
    @ComponentScan(basePackageClasses = {ExceptionHandlerAdvice.class})
    public static class TestConf {
    }

    @Test
    public void testExceptionHandlerClientAlreadyExist() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new ClientAlreadyExistException("petrov"));
        assertThat(result).isNotNull();
        log.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testExceptionHandlerClientCreate() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new ClientCreateException());
        assertThat(result).isNotNull();
        log.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testExceptionHandlerClientNotFound() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new ClientNotFoundException(1L));
        assertThat(result).isNotNull();
        log.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testExceptionHandlerPaymentNotFound() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new PaymentNotFoundException(1L));
        assertThat(result).isNotNull();
        log.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testExceptionHandlerIncorrectData() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new IncorrectDataException("test", 1L));
        assertThat(result).isNotNull();
        log.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.EXPECTATION_FAILED);
    }

    @Test
    public void testExceptionHandlerTransactionNotFound() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new TransactionNotFoundException(1L));
        assertThat(result).isNotNull();
        log.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testExceptionHandlerPaymentNotFoundExceptionWithId() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new PaymentNotFoundException(1L));
        assertThat(result).isNotNull();
        log.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testExceptionHandler() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new RuntimeException());
        assertThat(result).isNotNull();
        log.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.EXPECTATION_FAILED);
    }

}
