package onlinestore.inventoryservice.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ErrorDto {
    private final String errorMessage;
    private final LocalDateTime timestamp;

    public ErrorDto(String errorMessage) {
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now();
    }
}
