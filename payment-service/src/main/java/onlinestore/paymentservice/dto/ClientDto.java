package onlinestore.paymentservice.dto;

import lombok.Builder;
import lombok.Data;
import onlinestore.paymentservice.model.entity.ClientEntity;

import java.math.BigDecimal;

@Data
@Builder
public class ClientDto {
    private Long id;
    private String userName;
    private String name;
    private String account;
    private BigDecimal balance;

    public static ClientDto fromClientEntity(ClientEntity clientEntity) {
        return builder()
                .id(clientEntity.getId())
                .userName(clientEntity.getUserName())
                .name(clientEntity.getName())
                .account(clientEntity.getAccount() == null ? "" : clientEntity.getAccount().getNumber())
                .balance(clientEntity.getAccount() == null ? BigDecimal.valueOf(0.00) : clientEntity.getAccount().getBalance())
                .build();
    }

}
