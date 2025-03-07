package io.github.msj.doccave_notify.service;

import io.github.msj.doccave_notify.entity.NotificationLog;
import io.github.msj.doccave_notify.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationLoggerService {

    private final NotificationLogRepository notificationLogRepository;

    public void logSuccess(UUID userId, String message) {
        log.info("✅ Notificação enviada para {}: {}", userId, message);
        saveLog(userId, message, "SUCCESS", null);
    }

    public void logFailure(UUID userId, String message, String errorDetails) {
        log.error("❌ Erro ao enviar notificação para {}: {}", userId, errorDetails);
        saveLog(userId, message, "FAILED", errorDetails);
    }

    private void saveLog(UUID userId, String message, String status, String errorDetails) {
        NotificationLog log = NotificationLog.builder()
                .userId(userId)
                .message(message)
                .status(status)
                .errorDetails(errorDetails)
                .timestamp(LocalDateTime.now())
                .build();
        notificationLogRepository.save(log);
    }
}

