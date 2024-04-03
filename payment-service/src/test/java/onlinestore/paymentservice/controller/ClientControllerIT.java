package onlinestore.paymentservice.controller;

import onlinestore.paymentservice.BaseIT;
import onlinestore.paymentservice.dto.ClientAddBalanceDto;
import onlinestore.paymentservice.dto.ClientDto;
import onlinestore.paymentservice.dto.TransactionDto;
import onlinestore.paymentservice.model.entity.ClientEntity;
import onlinestore.paymentservice.model.repository.ClientRepository;
import onlinestore.paymentservice.model.repository.PaymentRepository;
import onlinestore.paymentservice.model.repository.TransactionRepository;
import onlinestore.paymentservice.service.ClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@Sql(scripts = "classpath:clearAll.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ClientControllerIT extends BaseIT {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    Logger logger = LoggerFactory.getLogger(ClientControllerIT.class);

    private ClientDto clientDto;

    private ClientAddBalanceDto clientAddBalanceDto;

    @MockBean
    private RestTemplate restTemplateMock;

    @BeforeEach
    public void setUp() {
        ClientEntity clientEntity = new ClientEntity("petrov", "Petrov Pavel Pavlovich");
        clientDto = ClientDto.fromClientEntity(clientEntity);
        clientDto.setBalance(BigDecimal.valueOf(200));
        clientAddBalanceDto = new ClientAddBalanceDto(clientEntity.getUserName(), BigDecimal.valueOf(1000));
    }

    @Test
    public void checkAddClient() {
        ResponseEntity<ClientDto> response = addClient();
        logger.info("response status {} \n\t {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertEquals(0, clientDto.getBalance().compareTo(response.getBody().getBalance()));
    }

    private ResponseEntity<ClientDto> addClient() {
        Mockito.when(restTemplateMock.exchange(ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any()))
                .thenReturn(new ResponseEntity<>("body", HttpStatus.OK));
        return restTemplate.exchange(
                "/client", HttpMethod.POST,
                new HttpEntity<>(clientDto, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }

    @Test
    public void checkAddBalance() {
        addClient();
        ResponseEntity<ClientDto> response = addBalance();
        logger.info("response status {}, body: \n\t {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue((response.getBody() != null));
        Assertions.assertEquals(0, clientDto.getBalance().add(clientAddBalanceDto.getAmount())
                .compareTo(response.getBody().getBalance()));
    }

    private ResponseEntity<ClientDto> addBalance() {
        return restTemplate.exchange(
                "/client/balance", HttpMethod.POST,
                new HttpEntity<>(clientAddBalanceDto, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }

    @Test
    public void checkGetTransactions() {
        addClient();
        addBalance();
        ResponseEntity<List<TransactionDto>> allTransactions = getAllTransaction();
        logger.info("response status {}, body: \n\t {}", allTransactions.getStatusCode(), allTransactions.getBody());
        Assertions.assertEquals(HttpStatus.OK, allTransactions.getStatusCode());
        Assertions.assertTrue((allTransactions.getBody() != null));
        Assertions.assertEquals(2, allTransactions.getBody().size());

        Long id = allTransactions.getBody().stream().findFirst().orElse(null).getId();
        ResponseEntity<TransactionDto> transaction = getTransaction(id);
        logger.info("response (id = {}) status {}, body: \n\t {}", id, transaction.getStatusCode(), transaction.getBody());
        Assertions.assertEquals(HttpStatus.OK, transaction.getStatusCode());
        Assertions.assertTrue((transaction.getBody() != null));
    }

    private ResponseEntity<List<TransactionDto>> getAllTransaction() {
        return restTemplate.exchange(
                "/transaction", HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }

    private ResponseEntity<TransactionDto> getTransaction(Long id) {
        String uriTemplate = "/transaction/{id}";
        URI uri = UriComponentsBuilder.fromUriString(uriTemplate).build(id);
        return restTemplate.exchange(
                uri, HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }

    @Test
    public void checkGetClients() throws Exception {
        addClient();
        ResponseEntity<List<ClientDto>> allClients = getAllClients();
        logger.info("response status {}, body: \n\t {}", allClients.getStatusCode(), allClients.getBody());
        Assertions.assertEquals(HttpStatus.OK, allClients.getStatusCode());
        Assertions.assertNotNull(allClients.getBody());
        Assertions.assertEquals(1, allClients.getBody().size());
        Long id = allClients.getBody().stream().findFirst().orElse(null).getId();

        ResponseEntity<ClientDto> client = getClient(id);
        logger.info("response (id = {}) status {}, body: \n\t {}", id, client.getStatusCode(), client.getBody());
        Assertions.assertEquals(HttpStatus.OK, client.getStatusCode());
        Assertions.assertNotNull(client.getBody());
        Assertions.assertEquals(0, clientDto.getBalance().compareTo(client.getBody().getBalance()));
    }

    private ResponseEntity<List<ClientDto>> getAllClients() {
        return restTemplate.exchange(
                "/client", HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }

    private ResponseEntity<ClientDto> getClient(Long id) throws Exception {
        String uriTemplate = "/client/{id}";
        URI uri = UriComponentsBuilder.fromUriString(uriTemplate).build(id);
        return restTemplate.exchange(
                uri, HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }
}
