package onlinestore.orderservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDto {

    private String errorMessage;

    private LocalDateTime timestamp;

    public ErrorDto(String errorMessage) {
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now();
    }
}
