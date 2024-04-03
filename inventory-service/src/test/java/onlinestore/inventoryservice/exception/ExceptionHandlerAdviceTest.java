package onlinestore.inventoryservice.exception;

import onlinestore.inventoryservice.dto.ErrorDto;
import onlinestore.inventoryservice.event.InventoryDocumentProcessorImpl;
import onlinestore.inventoryservice.exception.ExceptionHandlerAdvice;
import onlinestore.inventoryservice.exception.ProductCreateException;
import onlinestore.inventoryservice.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@WebMvcTest(ExceptionHandlerAdvice.class)
public class ExceptionHandlerAdviceTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ExceptionHandlerAdvice exceptionHandlerAdvice;

    Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @Configuration
    @ComponentScan(basePackageClasses = {ExceptionHandlerAdvice.class})
    public static class TestConf {
    }

    @Test
    public void testExceptionHandlerProductCreate() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new ProductCreateException());
        assertThat(result).isNotNull();
        logger.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_MODIFIED);
    }

    @Test
    public void testExceptionHandlerProductCreatePositive() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new ProductCreateException(0D));
        assertThat(result).isNotNull();
        logger.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_MODIFIED);
    }

    @Test
    public void testExceptionHandlerProductNotFoundWithArticle() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new ProductNotFoundException("Cream"));
        assertThat(result).isNotNull();
        logger.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testExceptionHandlerProductNotFoundWithId() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new ProductNotFoundException(1L));
        assertThat(result).isNotNull();
        logger.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testExceptionHandler() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new RuntimeException());
        assertThat(result).isNotNull();
        logger.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.EXPECTATION_FAILED);
    }

}
