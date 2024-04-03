package onlinestore.deliveryservice.event;

import onlinestore.deliveryservice.TestUtil;
import onlinestore.deliveryservice.dto.OrderDetailDto;
import onlinestore.deliveryservice.event.InventoryDocumentEvent;
import onlinestore.deliveryservice.model.repository.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@WebMvcTest(InventoryDocumentEvent.class)
public class InventoryDocumentEventTest {

    @MockBean
    private DeliveryRepository deliveryRepository;
    private InventoryDocumentEvent eventFirst;
    private InventoryDocumentEvent eventSecond;
    Logger logger = LoggerFactory.getLogger(InventoryDocumentEvent.class);


    @BeforeEach
    public void setUp() {
        eventFirst = TestUtil.getInstanceInventoryDocumentEvent();

        eventSecond = InventoryDocumentEvent.builder().build();
        eventSecond.setId(2L);
        eventSecond.setOrderId(2L);
        eventSecond.setDateCreated(LocalDateTime.now());
        eventSecond.setDateModified(eventSecond.getDateCreated());
        eventSecond.setUserName("petrov");
        eventSecond.setClientName("Petrov Pavel");
        eventSecond.setAmount(BigDecimal.valueOf(50));
        eventSecond.setOrderDataCreated(eventSecond.getDateCreated().minusDays(1));
        eventSecond.setDestinationAddress("Saint Petersburg, Nevsky Avenue, 1");
        eventSecond.setDescription("test Petrov");
        List<OrderDetailDto> details = new ArrayList<>();
        OrderDetailDto detail = OrderDetailDto.builder().build();
        detail.setProductArticle("Cream");
        detail.setPrice(BigDecimal.valueOf(50));
        detail.setQuantity(1D);
        detail.setAmount(BigDecimal.valueOf(50));
        eventSecond.setDetails(details);
    }

    @Test
    public void testInventoryDocumentEventData() {
        logger.info("First inventory document hash {}: {}", eventFirst.hashCode(), eventFirst);
        logger.info("Second inventory document {}: {}", eventSecond.hashCode(), eventSecond);
        InventoryDocumentEvent eventRef = eventSecond;
        assertThat(eventFirst.equals(eventSecond)).isFalse();
        assertThat(eventRef.equals(eventSecond)).isTrue();

        assertThat(eventFirst.hashCode()).isNotEqualTo(eventSecond.hashCode());
        assertThat(eventFirst.getId()).isNotEqualTo(eventSecond.getId());
        assertThat(eventFirst.getOrderId()).isNotEqualTo(eventSecond.getOrderId());
        assertThat(eventFirst.getEvent()).isEqualTo(eventSecond.getEvent());
        assertThat(eventFirst.getAmount()).isEqualTo(eventSecond.getAmount());
        assertThat(eventFirst.getUserName()).isEqualTo(eventSecond.getUserName());
        assertThat(eventFirst.getClientName()).isEqualTo(eventSecond.getClientName());
        assertThat(eventFirst.getDateCreated()).isNotEqualTo(eventSecond.getDateCreated());
        assertThat(eventFirst.getDateModified()).isNotEqualTo(eventSecond.getDateModified());
        assertThat(eventFirst.getDestinationAddress()).isEqualTo(eventSecond.getDestinationAddress());
        assertThat(eventFirst.getDescription()).isEqualTo(eventSecond.getDescription());
    }


}
