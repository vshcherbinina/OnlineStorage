package onlinestore.orderservice.exception;

import lombok.extern.slf4j.Slf4j;
import onlinestore.orderservice.dto.ErrorDto;
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
    public void testExceptionHandlerIncorrectInput() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new IncorrectInputException("test"));
        assertThat(result).isNotNull();
        log.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testExceptionHandlerOrderNotFound() {
        ResponseEntity<ErrorDto> result = exceptionHandlerAdvice.exceptionHandler(new OrderNotFoundException(1L));
        assertThat(result).isNotNull();
        log.info("Response: {}", result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
