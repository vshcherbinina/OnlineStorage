package onlinestore.paymentservice;

import onlinestore.paymentservice.controller.ClientController;
import onlinestore.paymentservice.dto.ClientAddBalanceDto;
import onlinestore.paymentservice.dto.ClientDto;
import onlinestore.paymentservice.model.entity.AccountEntity;
import onlinestore.paymentservice.model.entity.ClientEntity;
import onlinestore.paymentservice.model.repository.ClientRepository;
import onlinestore.paymentservice.model.repository.PaymentRepository;
import onlinestore.paymentservice.model.repository.TransactionRepository;
import onlinestore.paymentservice.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ClientService clientService;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private PaymentRepository paymentRepository;

    @Configuration
    @ComponentScan(basePackageClasses = {ClientController.class})
    public static class TestConf {
    }

    private ClientEntity clientFirst;

    private ClientEntity clientSecond;

    ClientDto clientDto;

    ClientAddBalanceDto addBalanceDto;

    private List<ClientDto> clients;

    @BeforeEach
    public void setUp() {
        clientFirst = new ClientEntity("petrov", "Petrov");
        AccountEntity accountFirst = new AccountEntity(clientFirst);
        clientFirst.setAccount(accountFirst);

        clientSecond = new ClientEntity("ivanov", "Ivanov");
        AccountEntity accountSecond = new AccountEntity(clientSecond);
        clientSecond.setAccount(accountSecond);

        clientDto = ClientDto.fromClientEntity(clientSecond);
        addBalanceDto = new ClientAddBalanceDto(clientSecond.getUserName(), 1000D);

        clients = Collections.singletonList(ClientDto.fromClientEntity(clientFirst));
    }

    @Test
    public void getAllClients() throws Exception {
        Mockito.when(clientService.getAllClients()).thenReturn(clients);
        mvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(clientFirst.getName())));
    }

    @Test
    public void getClient() throws Exception {
        Mockito.when(clientService.getClientById(1L)).thenReturn(ClientDto.fromClientEntity(clientFirst));
        mvc.perform(get("/client/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(clientFirst.getName())));
    }

    @Test
    public void addClient() throws Exception {
        Mockito.when(clientService.createClient(clientDto)).thenReturn(ClientDto.fromClientEntity(clientSecond));
        mvc.perform(
                post("/client")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"ivanov\", \"name\": \"Ivanov\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void addClientBalance() throws Exception {
        Mockito.when(clientService.addBalance(addBalanceDto)).thenReturn(clientDto);
        mvc.perform(
                    post("/client/balance")
                            .accept(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "    \"userName\": \"ivanov\",\n" +
                                    "    \"amount\": 1000\n" +
                                    "}  ")
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

}
