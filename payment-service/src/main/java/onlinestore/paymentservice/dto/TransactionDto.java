package onlinestore.paymentservice.dto;

import lombok.Builder;
import lombok.Data;
import onlinestore.paymentservice.model.entity.TransactionEntity;

import static onlinestore.paymentservice.dto.PaymentDto.fromPaymentEntity;

@Data
@Builder
public class TransactionDto {
    private Long id;
    private String account;
    private int income;
    private Double amount;
    private String date;
    private PaymentDto payment;

    public static TransactionDto fromTransactionEntity(TransactionEntity transaction) {
        return builder()
                .id(transaction.getId())
                .account(transaction.getAccount().getNumber())
                .income(transaction.getIncome())
                .amount(transaction.getAmount())
                .date(transaction.getDate().toString())
                .payment(transaction.getPayment() == null ? null : fromPaymentEntity(transaction.getPayment()))
                .build();
    }
}
