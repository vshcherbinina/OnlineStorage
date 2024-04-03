package onlinestore.paymentservice.service;

import onlinestore.paymentservice.dto.ClientAddBalanceDto;
import onlinestore.paymentservice.dto.ClientDto;
import onlinestore.paymentservice.exception.ClientNotFoundException;
import onlinestore.paymentservice.model.entity.ClientEntity;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.util.List;

public interface ClientService {
    ClientEntity getClientByUserName(String userName) throws ClientNotFoundException;

    ClientDto createClient(ClientDto clientDto, HttpHeaders headers);

    ClientDto getClientById(Long id) throws ClientNotFoundException, IllegalArgumentException;

    void createTransactionAndChangeBalance(ClientEntity client, BigDecimal amount, boolean income);

    List<ClientDto> getAllClients();

    ClientDto addBalance (ClientAddBalanceDto input) throws ClientNotFoundException, IllegalArgumentException;

}
