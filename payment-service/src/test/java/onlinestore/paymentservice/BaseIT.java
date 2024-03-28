package onlinestore.paymentservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = PaymentServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
public abstract class BaseIT {
    private static final String DATABASE_NAME = "payment_db";
    @Autowired
    public TestRestTemplate restTemplate;

    @Container
    private static final PostgreSQLContainer<?> dataContainer;

    static {
        dataContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName(DATABASE_NAME)
                .withUsername("test_user")
                .withPassword("test_pass")
                .withReuse(true);
        dataContainer.start();
    }

    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", dataContainer::getJdbcUrl);
        registry.add("spring.datasource.password", dataContainer::getPassword);
        registry.add("spring.datasource.username", dataContainer::getUsername);
    }

}
