package onlinestore.paymentservice;

import onlinestore.paymentservice.dto.OrderDetailDto;
import onlinestore.paymentservice.dto.OrderStatusDto;
import onlinestore.paymentservice.event.OrderEvent;
import onlinestore.paymentservice.event.OrderStatusEvent;
import onlinestore.paymentservice.model.entity.AccountEntity;
import onlinestore.paymentservice.model.entity.ClientEntity;
import onlinestore.paymentservice.model.entity.OrderDataEntity;
import onlinestore.paymentservice.model.entity.PaymentEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static void setAccount(ClientEntity client) {
        AccountEntity account = new AccountEntity(client);
        account.setId(client.getId());
        client.setAccount(account);
    }

    public static ClientEntity getInstanceClientFirst() {
        ClientEntity clientPetrov = new ClientEntity("first", "Client First");
        clientPetrov.setId(1L);
        setAccount(clientPetrov);
        return clientPetrov;
    }

    public static ClientEntity getInstanceClientSecond() {
        ClientEntity clientIvanov = new ClientEntity("second", "Client Second");
        clientIvanov.setId(2L);
        setAccount(clientIvanov);
        return clientIvanov;
    }

    public static OrderStatusEvent getInstanceOrderStatusEvent() {
        return OrderStatusEvent.builder()
                .orderId(1L)
                .status(OrderStatusDto.PAID)
                .dateModified(LocalDateTime.now())
                .statusDescription("test")
                .build();
    }

    public static OrderEvent getInstanceOrderEvent(ClientEntity client ) {
        OrderEvent orderEvent = OrderEvent.builder()
                .id(client.getId())
                .status(OrderStatusDto.REGISTERED)
                .dateCreated(LocalDateTime.now())
                .userName(client.getUserName())
                .clientName(client.getName())
                .destinationAddress("Saint Petersburg, Nevsky Avenue, 1")
                .amount(BigDecimal.valueOf(100))
                .description("test")
                .build();
        orderEvent.setDateModified(orderEvent.getDateCreated());
        List<OrderDetailDto> details = new ArrayList<>();
        OrderDetailDto detailDto = OrderDetailDto.builder()
                .productArticle("Cream")
                .price(BigDecimal.valueOf(100))
                .quantity(2D)
                .amount(BigDecimal.valueOf(50))
                .build();
        details.add(detailDto);
        orderEvent.setDetails(details);
        return orderEvent;
    }

    public static PaymentEntity getInstancePaymentEntity(ClientEntity client) {
        OrderEvent orderEvent = getInstanceOrderEvent(client);
        PaymentEntity payment = new PaymentEntity(orderEvent.getAmount(), orderEvent.getId());
        payment.setAccount(client.getAccount());
        payment.setId(client.getId());
        return payment;
    }

}
