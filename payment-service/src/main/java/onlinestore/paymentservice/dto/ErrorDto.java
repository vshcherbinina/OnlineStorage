package onlinestore.paymentservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorDto {

    private String errorMessage;

    private LocalDateTime timestamp;

    public ErrorDto(String errorMessage) {
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now();
    }
}
