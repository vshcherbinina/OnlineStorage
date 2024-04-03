package onlinestore.paymentservice.controller;

import lombok.extern.slf4j.Slf4j;
import onlinestore.paymentservice.TestUtil;
import onlinestore.paymentservice.dto.ClientAddBalanceDto;
import onlinestore.paymentservice.model.entity.AccountEntity;
import onlinestore.paymentservice.model.entity.ClientEntity;
import onlinestore.paymentservice.model.repository.*;
import onlinestore.paymentservice.model.util.RepositoryUtil;
import onlinestore.paymentservice.service.ClientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RepositoryUtil repositoryUtil;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private PaymentRepository paymentRepository;
    @MockBean
    private OrderDataRepository orderDataRepository;
    @MockBean
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ClientService clientService;
    @MockBean
    private RestTemplate restTemplateMock;

    @Configuration
    @ComponentScan(basePackageClasses = {ClientController.class, ClientService.class, RepositoryUtil.class})
    public static class TestConf {
    }

    private ClientEntity clientFirst;
    private List<ClientEntity> clients;

    @BeforeEach
    public void setUp() {
        clientFirst = TestUtil.getInstanceClientFirst();
        clients = Collections.singletonList(clientFirst);
    }

    @Test
    @DisplayName("List all clients")
    public void testGetAllClients() throws Exception {
        Mockito.when(clientRepository.findAll()).thenReturn(clients);
        Mockito.when(accountRepository.findByClientId(1L)).thenReturn(Optional.of(clientFirst.getAccount()));
        mvc.perform(get("/client"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(clientFirst.getName())));
    }

    @Test
    @DisplayName("Get client by id")
    public void testGetClient() throws Exception {
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(clientFirst));
        Mockito.when(accountRepository.findByClientId(1L)).thenReturn(Optional.of(clientFirst.getAccount()));
        mvc.perform(get("/client/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(clientFirst.getName())));
    }

    @Test
    @DisplayName("Add client")
    public void testAddClient() throws Exception {
        Mockito.when(clientRepository.save(any(ClientEntity.class))).thenReturn(clientFirst);
        Mockito.when(accountRepository.save(any(AccountEntity.class))).thenReturn(clientFirst.getAccount());
        Mockito.when(clientRepository.findByUserName(clientFirst.getUserName())).thenReturn(Optional.empty());
        Mockito.when(restTemplateMock.exchange(ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any()))
                .thenReturn(new ResponseEntity<>("body", HttpStatus.OK));
        mvc.perform(
                post("/client")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"first\", \"name\": \"Client First\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Add amount to user's account")
    public void addClientBalance() throws Exception {
        ClientAddBalanceDto addBalanceDto = new ClientAddBalanceDto(clientFirst.getUserName(), BigDecimal.valueOf(1000));
        Mockito.when(clientRepository.findByUserName(any(String.class))).thenReturn(Optional.of(clientFirst));
        Mockito.when(accountRepository.findByClientId(any(Long.class))).thenReturn(Optional.of(clientFirst.getAccount()));
        mvc.perform(
                        post("/client/balance")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"userName\": \"first\",\n" +
                                        "    \"amount\": 1000\n" +
                                        "}  ")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Add amount to user's account, with error 'client not found'")
    public void addClientBalanceNotFound() throws Exception {
        mvc.perform(
                        post("/client/balance")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"userName\": \"first\",\n" +
                                        "    \"amount\": 1000\n" +
                                        "}  ")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    @DisplayName("Test clientEntity hashCode")
    public void testClientEntityHashCode() {
        log.info("clientFirst: {}, hash: {}", clientFirst, clientFirst.hashCode());
        ClientEntity clientIvanov = TestUtil.getInstanceClientSecond();
        Assertions.assertThat(clientFirst.equals(clientIvanov)).isFalse();
    }

}
