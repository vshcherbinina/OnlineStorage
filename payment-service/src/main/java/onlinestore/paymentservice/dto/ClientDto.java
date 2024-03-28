package onlinestore.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.paymentservice.model.entity.ClientEntity;

@Data
@AllArgsConstructor
@Builder
public class ClientDto {
    private Long id;
    private String userName;
    private String name;
    private String account;
    private Double balance;

    public static ClientDto fromClientEntity(ClientEntity clientEntity) {
        return builder()
                .id(clientEntity.getId())
                .userName(clientEntity.getUserName())
                .name(clientEntity.getName())
                .account(clientEntity.getAccount() == null ? "" : clientEntity.getAccount().getNumber())
                .balance(clientEntity.getAccount() == null ? 0D : clientEntity.getAccount().getBalance())
                .build();
    }

}
