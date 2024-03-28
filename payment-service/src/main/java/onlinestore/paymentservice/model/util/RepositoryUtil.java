package onlinestore.paymentservice.model.util;

import lombok.Data;
import onlinestore.paymentservice.event.OrderEvent;
import onlinestore.paymentservice.event.OrderStatusEvent;
import onlinestore.paymentservice.exception.IncorrectDataException;
import onlinestore.paymentservice.exception.PaymentNotFoundException;
import onlinestore.paymentservice.model.entity.*;
import onlinestore.paymentservice.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class RepositoryUtil {

    private final OrderDataRepository orderDataRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final PaymentRepository paymentRepository;

    private final TransactionRepository transactionRepository;

    private final ClientRepository clientRepository;

    private final AccountRepository accountRepository;

    private final LockByKey lockByAccountId;

    @Autowired
    public RepositoryUtil(OrderDataRepository orderDataRepository, OrderDetailRepository orderDetailRepository,
                          PaymentRepository paymentRepository, TransactionRepository transactionRepository,
                          ClientRepository clientRepository, AccountRepository accountRepository, LockByKey lockByAccountId) {
        this.orderDataRepository = orderDataRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.lockByAccountId = lockByAccountId;
    }

    public void saveOrderData(OrderEvent event) {
        OrderDataEntity orderData = OrderDataEntity.fromOrderEvent(event);
        OrderDataEntity savedOrderData = orderDataRepository.save(orderData);
        orderData.setId(savedOrderData.getId());
        orderData.getDetails().forEach(d -> {
            d.setOrderData(orderData);
            orderDetailRepository.save(d);
        });

    }

    public void updateOrderData(OrderStatusEvent event) {
        OrderDataEntity orderData = orderDataRepository.findByOrderId(event.getOrderId())
                .orElseThrow(() -> new IncorrectDataException("order", event.getOrderId()));
        orderData.setDateModified(event.getDateModified());
        orderData.setStatus(event.getStatus());
        orderDataRepository.save(orderData);
    }

    public OrderEvent getOrderData(Long orderId) throws IllegalArgumentException {
        OrderDataEntity orderData = orderDataRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IncorrectDataException("order", orderId));
        List<OrderDetailEntity> details = orderDetailRepository.findAllByOrderDataId(orderData.getId()).orElse(new ArrayList<>());
        orderData.setDetails(details);
        OrderEvent orderEvent = OrderEvent.fromOrderDataEntity(orderData);
        orderEvent.setClientName(clientRepository.getClientNameByUserName(orderData.getUserName()));
        return orderEvent;
    }

    public void updatePaymentStatus(PaymentEntity payment, PaymentStatus status) {
        payment.setStatusAndDateModification(status);
        PaymentEntity savedPayment = paymentRepository.save(payment);
        payment.setId(savedPayment.getId());
    }

    public void declinePayment(PaymentEntity payment, String textError) {
        if (payment.getStatus().toString().equals(PaymentStatus.APPROVED.toString())) {
            createTransactionAndChangeBalance(payment.getAccount().getClient(), payment.getAmount(), true);
        }
        payment.setStatusDescription(textError);
        updatePaymentStatus(payment, PaymentStatus.DECLINED);
    }

    public void declinePaymentByOrderId(Long orderId, String textError) throws PaymentNotFoundException, IncorrectDataException{
        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException(orderId, textError));
        AccountEntity account = accountRepository.findByClientId(payment.getAccount().getClient().getId())
                .orElseThrow(() -> new IncorrectDataException("account", payment.getAccount().getClient().getId()));
        payment.getAccount().getClient().setAccount(account);
        declinePayment(payment, textError);
    }

    private void changeBalance(TransactionEntity transaction) {
        AccountEntity account = transaction.getAccount();
        Double changedBalance = account.getBalance() +
                transaction.getIncome() * transaction.getAmount();
        account.setBalance(changedBalance);
        accountRepository.save(account);
    }

    public void deductUserBalance(PaymentEntity payment) {
        try {
            lockByAccountId.lock(payment.getAccount().getId());
            if (payment.getAccount().getBalance().compareTo(payment.getAmount()) < 0) {
                declinePayment(payment, "Insufficient account balance");
            } else {
                TransactionEntity transaction = new TransactionEntity(payment);
                transactionRepository.save(transaction);
                changeBalance(transaction);
                updatePaymentStatus(payment, PaymentStatus.APPROVED);
            }
        } finally {
            lockByAccountId.unlock(payment.getAccount().getId());
        }
    }

    public void createTransactionAndChangeBalance(ClientEntity client, Double amount, boolean income) {
        try {
            lockByAccountId.lock(client.getAccount().getId());
            TransactionEntity transaction = new TransactionEntity(client.getAccount(), amount, income ? 1 : -1);
            transactionRepository.save(transaction);
            changeBalance(transaction);
        } finally {
            lockByAccountId.unlock(client.getAccount().getId());
        }
    }
}
