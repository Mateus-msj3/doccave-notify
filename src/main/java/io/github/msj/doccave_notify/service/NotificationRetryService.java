package io.github.msj.doccave_notify.service;

import io.github.msj.doccave_notify.dto.NotificationDTO;
import io.github.msj.doccave_notify.entity.NotificationLog;
import io.github.msj.doccave_notify.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationRetryService {

    private final NotificationLogRepository notificationLogRepository;
    private final NotificationService notificationService;
    private final NotificationLoggerService notificationLoggerService;

    @Scheduled(cron = "0 0 * * * ?") // A cada 1 hora
    public void retryFailedNotifications() {
        List<NotificationLog> failedNotifications = notificationLogRepository.findFailedNotifications();

        for (NotificationLog notificationLog : failedNotifications) {
            try {
                notificationService.sendNotification(NotificationDTO.builder()
                        .userId(notificationLog.getUserId())
                        .message(notificationLog.getMessage())
                        .errorDetails(notificationLog.getErrorDetails())
                        .timestamp(notificationLog.getTimestamp())
                        .status(notificationLog.getStatus())
                        .build());
                notificationLoggerService.logSuccess(notificationLog.getUserId(), notificationLog.getMessage());
            } catch (Exception e) {
                log.error("❌ Falha ao reprocessar notificação para {}: {}", notificationLog.getUserId(), e.getMessage());
            }
        }
    }
}

