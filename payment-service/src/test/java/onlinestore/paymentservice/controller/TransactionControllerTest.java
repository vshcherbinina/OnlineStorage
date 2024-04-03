package onlinestore.paymentservice.controller;

import lombok.extern.slf4j.Slf4j;
import onlinestore.paymentservice.TestUtil;
import onlinestore.paymentservice.model.entity.ClientEntity;
import onlinestore.paymentservice.model.entity.PaymentEntity;
import onlinestore.paymentservice.model.entity.TransactionEntity;
import onlinestore.paymentservice.model.repository.*;
import onlinestore.paymentservice.model.util.RepositoryUtil;
import onlinestore.paymentservice.service.ClientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RepositoryUtil repositoryUtil;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private PaymentRepository paymentRepository;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private OrderDataRepository orderDataRepository;
    @MockBean
    private OrderDetailRepository orderDetailRepository;

    @Configuration
    @ComponentScan(basePackageClasses = {ClientController.class, ClientService.class, RepositoryUtil.class})
    public static class TestConf {
    }

    private ClientEntity clientFirst;
    private PaymentEntity paymentFirst;
    private TransactionEntity transactionFirst;

    private ClientEntity clientSecond;
    private PaymentEntity paymentSecond;
    private TransactionEntity transactionSecond;

    private List<PaymentEntity> payments;
    private List<TransactionEntity> transactions;

    @MockBean
    private RestTemplate restTemplateMock;

    @BeforeEach
    public void setUp() {
        clientFirst = TestUtil.getInstanceClientFirst();
        paymentFirst = TestUtil.getInstancePaymentEntity(clientFirst);
        transactionFirst = new TransactionEntity(paymentFirst);
        transactionFirst.setId(paymentFirst.getId());

        clientSecond = TestUtil.getInstanceClientSecond();
        paymentSecond = TestUtil.getInstancePaymentEntity(clientSecond);
        transactionSecond = new TransactionEntity(paymentSecond);
        transactionSecond.setId(paymentSecond.getId());

        payments = new ArrayList<>();
        payments.add(paymentFirst);
        payments.add(paymentSecond);

        transactions = new ArrayList<>();
        transactions.add(transactionFirst);
        transactions.add(transactionSecond);
    }

    @Test
    @DisplayName("List all transactions")
    public void testGetTransactions() throws Exception {
        Mockito.when(transactionRepository.findAll()).thenReturn(transactions);
        mvc.perform(get("/transaction"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get transaction by id")
    public void testGetTransactionById() throws Exception {
        Mockito.when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.of(transactionFirst));
        URI uri = UriComponentsBuilder.fromUriString("/transaction/{id}").build(1L);
        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("List all payments")
    public void testGetPayments() throws Exception {
        Mockito.when(paymentRepository.findAll()).thenReturn(payments);
        mvc.perform(get("/payment"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get payment by id")
    public void testGetPaymentById() throws Exception {
        Mockito.when(paymentRepository.findById(any(Long.class))).thenReturn(Optional.of(paymentFirst));
        URI uri = UriComponentsBuilder.fromUriString("/payment/{id}").build(1L);
        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test transactionEntity hashCode")
    public void testTransactionEntityHashCode() {
        log.info("transactionFirst: {}, hash: {}", transactionFirst, transactionFirst.hashCode());
        log.info("transactionSecond: {}, hash: {}", transactionFirst, transactionSecond.hashCode());
        Assertions.assertThat(transactionFirst.equals(transactionSecond)).isFalse();
    }

    @Test
    @DisplayName("Test paymentEntity hashCode")
    public void testPaymentEntityHashCode() {
        log.info("paymentFirst: {}, hash: {}", paymentFirst, paymentFirst.hashCode());
        log.info("paymentSecond: {}, hash: {}", paymentSecond, paymentSecond.hashCode());
        Assertions.assertThat(paymentFirst.equals(paymentSecond)).isFalse();
    }
}
