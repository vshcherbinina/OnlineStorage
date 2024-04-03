package onlinestore.paymentservice.service;


import lombok.extern.slf4j.Slf4j;
import onlinestore.paymentservice.config.RestTemplateConfig;
import onlinestore.paymentservice.dto.ClientAddBalanceDto;
import onlinestore.paymentservice.dto.ClientDto;
import onlinestore.paymentservice.exception.ClientAlreadyExistException;
import onlinestore.paymentservice.exception.ClientCreateException;
import onlinestore.paymentservice.exception.ClientNotFoundException;
import onlinestore.paymentservice.exception.IncorrectDataException;
import onlinestore.paymentservice.model.entity.AccountEntity;
import onlinestore.paymentservice.model.entity.ClientEntity;
import onlinestore.paymentservice.model.util.RepositoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService{

    private final RepositoryUtil repositoryUtil;
    private final RestTemplate restTemplate;
    private final String authGetUserUri;


    @Autowired
    public ClientServiceImpl(RepositoryUtil repositoryUtil, @Value("${gateway.uri}") String gatewayUri, RestTemplate restTemplate) {
        this.repositoryUtil = repositoryUtil;
        this.authGetUserUri = gatewayUri + "/auth/user/{username}";
        this.restTemplate = restTemplate;
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
                .orElseThrow(() -> new IncorrectDataException("account", client.getId())));

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
    public ClientDto createClient(ClientDto clientDto, HttpHeaders headers) {
        if (clientDto.getUserName().isBlank()) {
            throw new ClientCreateException();
        }
        URI uri = UriComponentsBuilder.fromUriString(authGetUserUri).build(clientDto.getUserName());
        log.info("Request get to auth: {}", uri);
        RequestEntity<Void> requestEntity = RequestEntity.get(uri)
                .headers(headers)
                .build();
        try {
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
            log.info("Response from auth: {}", response);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ClientCreateException(clientDto.getUserName());
            }
        } catch (Exception e) {
            log.error("Response from auth with error: {}", e.toString());
            throw new ClientCreateException(clientDto.getUserName());
        }

        return ClientDto.fromClientEntity(createClient(clientDto.getUserName(), clientDto.getName(), clientDto.getBalance()));
    }

    @Transactional
    private ClientEntity createClient(String userName, String name, BigDecimal balance) {
        ClientEntity client = repositoryUtil.getClientRepository().findByUserName(userName)
                .orElse(new ClientEntity(userName, name));
        if (client.getId() != null && client.getId().compareTo(0L) > 0) {
            throw new ClientAlreadyExistException(userName);
        }

        ClientEntity savedClient = repositoryUtil.getClientRepository().save(client);
        createAccount(savedClient);
        if (balance != null && balance.compareTo(BigDecimal.valueOf(0.00)) > 0) {
            createTransactionAndChangeBalance(savedClient, balance, true);
        }
        return savedClient;
    }

    @Transactional
    @Override
    public ClientDto addBalance(ClientAddBalanceDto input) throws ClientNotFoundException, IllegalArgumentException {
        ClientEntity client = getClientByUserName(input.getUserName());
        createTransactionAndChangeBalance(client, input.getAmount(), true);
        return ClientDto.fromClientEntity(client);
    }

    @Override
    public void createTransactionAndChangeBalance(ClientEntity client, BigDecimal amount, boolean income) {
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
