package onlinestore.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import onlinestore.paymentservice.dto.PaymentDto;
import onlinestore.paymentservice.dto.TransactionDto;
import onlinestore.paymentservice.model.entity.PaymentEntity;
import onlinestore.paymentservice.model.entity.TransactionEntity;
import onlinestore.paymentservice.model.repository.PaymentRepository;
import onlinestore.paymentservice.model.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository, PaymentRepository paymentRepository) {
        this.transactionRepository = transactionRepository;
        this.paymentRepository = paymentRepository;
    }

    @Operation(summary = "List all transaction", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/transaction")
    public List<TransactionDto> getTransactions() {
        return transactionRepository.findAll().stream().sorted(Comparator.comparing(TransactionEntity::getId))
                .map(TransactionDto::fromTransactionEntity).toList();
    }

    @Operation(summary = "Get transaction by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/transaction/{id}")
    public TransactionDto getTransactionById(@PathVariable @Parameter(description = "Id of transaction") Long id) {
        return transactionRepository.findById(id)
                .map(TransactionDto::fromTransactionEntity)
                .orElse(null);
    }

    @Operation(summary = "List all payments", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/payment")
    public List<PaymentDto> getPayments() {
        return paymentRepository.findAll().stream().sorted(Comparator.comparing(PaymentEntity::getId))
                .map(PaymentDto::fromPaymentEntity).toList();
    }

    @Operation(summary = "Get payment by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/payment/{id}")
    public PaymentDto getPaymentById(@PathVariable @Parameter(description = "Id of payment") Long id) {
        return paymentRepository.findById(id)
                .map(PaymentDto::fromPaymentEntity)
                .orElse(null);
    }

}
