package io.github.msj.doccave_notify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NotificationDTO {

    private UUID userId;

    private String message;

    private String status;

    private String errorDetails;

    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "userId=" + userId +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", errorDetails='" + errorDetails + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
