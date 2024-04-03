package onlinestore.deliveryservice.controller;

import onlinestore.deliveryservice.TestUtil;
import onlinestore.deliveryservice.model.entity.DeliveryEntity;
import onlinestore.deliveryservice.model.repository.DeliveryRepository;
import onlinestore.deliveryservice.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(DeliveryController.class)
public class DeliveryControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private DeliveryService deliveryService;
    @MockBean
    private DeliveryRepository deliveryRepository;

    @Configuration
    @ComponentScan(basePackageClasses = {DeliveryController.class, DeliveryService.class})
    public static class TestConf {
    }

    private DeliveryEntity document;
    private List<DeliveryEntity> documents;
    Logger logger = LoggerFactory.getLogger(DeliveryControllerTest.class);


    @BeforeEach
    public void setUp() {
        document = TestUtil.getInstanceDeliveryEntity();
        documents = Collections.singletonList(document);
    }

    @Test
    @DisplayName("List all deliveries")
    public void testListDeliveries() throws Exception {
        Mockito.when(deliveryRepository.findAll()).thenReturn(documents);
        mvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get inventory document by id")
    public void testGetInventoryDocument() throws Exception {
        Mockito.when(deliveryRepository.findById(any(Long.class))).thenReturn(Optional.of(document));
        URI uri = UriComponentsBuilder.fromUriString("/delivery/{id}").build(1);
        logger.info("Document hash {}: {}", document.hashCode(), document);
        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(document.getStatus().toString())));
    }

    @Test
    @DisplayName("Get inventory document by id")
    public void testGetInventoryDocumentWithError() throws Exception {
        Mockito.doThrow(new RuntimeException("test error")).when(deliveryRepository).findById(1L);
        URI uri = UriComponentsBuilder.fromUriString("/delivery/{id}").build(1);
        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

}
