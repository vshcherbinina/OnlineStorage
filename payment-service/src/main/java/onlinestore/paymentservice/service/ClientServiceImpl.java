package onlinestore.paymentservice.service;


import onlinestore.paymentservice.dto.ClientAddBalanceDto;
import onlinestore.paymentservice.dto.ClientDto;
import onlinestore.paymentservice.exception.ClientAlreadyExistException;
import onlinestore.paymentservice.exception.ClientCreateException;
import onlinestore.paymentservice.exception.ClientNotFoundException;
import onlinestore.paymentservice.model.entity.AccountEntity;
import onlinestore.paymentservice.model.entity.ClientEntity;
import onlinestore.paymentservice.model.util.RepositoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService{

    private final RepositoryUtil repositoryUtil;

    @Autowired
    public ClientServiceImpl(RepositoryUtil repositoryUtil) {
        this.repositoryUtil = repositoryUtil;
    }

    @Override
    public ClientEntity getClientByUserName(String userName) throws ClientNotFoundException, IllegalArgumentException{
        ClientEntity client = repositoryUtil.getClientRepository().findByUserName(userName)
                .orElseThrow(() -> new ClientNotFoundException(userName));
        setClientAccount(client);
        return client;
    }

    private void setClientAccount(ClientEntity client) throws IllegalArgumentException {
        client.setAccount(repositoryUtil.getAccountRepository().findByClientId(client.getId())
                .orElseThrow(() -> new IllegalArgumentException("Incorrect data - client account not found")));

    }

    @Override
    public ClientDto getClientById(Long id) throws ClientNotFoundException, IllegalArgumentException {
        ClientEntity client = repositoryUtil.getClientRepository().findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        setClientAccount(client);
        return ClientDto.fromClientEntity(client);
    }

    private void createAccount(ClientEntity client) {
        AccountEntity account = new AccountEntity(client);
        AccountEntity accountSaved = repositoryUtil.getAccountRepository().save(account);
        account.setId(accountSaved.getId());
        client.setAccount(account);
    }

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        if (clientDto.getUserName().isBlank()) {
            throw new ClientCreateException();
        }
        return ClientDto.fromClientEntity(createClient(clientDto.getUserName(), clientDto.getName(), clientDto.getBalance()));
    }

    private ClientEntity createClient(String userName, String name, Double balance) {
        ClientEntity client = repositoryUtil.getClientRepository().findByUserName(userName)
                .orElse(new ClientEntity(userName, name));
        if (client.getId() != null && client.getId().compareTo(0L) > 0) {
            throw new ClientAlreadyExistException(userName);
        }

        ClientEntity savedClient = repositoryUtil.getClientRepository().save(client);
        createAccount(savedClient);
        if (balance != null && balance.compareTo(0D) > 0) {
            createTransactionAndChangeBalance(savedClient, balance, true);
        }
        return savedClient;
    }

    @Override
    public ClientDto addBalance(ClientAddBalanceDto input) throws ClientNotFoundException, IllegalArgumentException {
        ClientEntity client = getClientByUserName(input.getUserName());
        createTransactionAndChangeBalance(client, input.getAmount(), true);
        return ClientDto.fromClientEntity(client);
    }

    @Override
    public void createTransactionAndChangeBalance(ClientEntity client, Double amount, boolean income) {
        repositoryUtil.createTransactionAndChangeBalance(client, amount, income);
    }

    @Override
    public List<ClientDto> getAllClients() {
        List<ClientDto> clientDtoList = new ArrayList<>();
        repositoryUtil.getClientRepository().findAll().forEach(client -> {
            setClientAccount(client);
            clientDtoList.add(ClientDto.fromClientEntity(client));
        });
        return clientDtoList;
    }

}
