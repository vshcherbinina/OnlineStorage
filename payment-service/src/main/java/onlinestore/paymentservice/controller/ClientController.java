package onlinestore.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import onlinestore.paymentservice.dto.ClientAddBalanceDto;
import onlinestore.paymentservice.dto.ClientDto;
import onlinestore.paymentservice.dto.ErrorDto;
import onlinestore.paymentservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Slf4j
@RestController
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "List all clients", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/client")
    public List<ClientDto> getAllClients() {
        List<ClientDto> clientDtoList = clientService.getAllClients();
        clientDtoList.sort(Comparator.comparing(ClientDto::getId));
        return clientDtoList;
    }

    @Operation(summary = "Get client by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/client/{id}")
    public ResponseEntity<?> getClient(@PathVariable @Parameter(description = "Client id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clientService.getClientById(id));
    }

    @Operation(summary = "Add client", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/client")
    public ResponseEntity<?> addClient(@RequestHeader HttpHeaders headers, @RequestBody ClientDto input) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.createClient(input, headers));
    }

    @Operation(summary = "Add amount to user's account", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/client/balance")
    public ResponseEntity<?> addBalance(@RequestBody ClientAddBalanceDto input) {
        try {
            ClientDto clientDto = clientService.addBalance(input);
            return ResponseEntity.status(HttpStatus.OK).body(clientDto);
        } catch (Exception ex) {
            log.error("Can't change balance for client with userName {}", input.getUserName(), ex);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new ErrorDto(ex.getMessage()));
        }
    }

}
