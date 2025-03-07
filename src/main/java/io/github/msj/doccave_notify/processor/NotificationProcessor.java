package io.github.msj.doccave_notify.processor;

import io.github.msj.doccave_notify.dto.NotificationDTO;
import io.github.msj.doccave_notify.service.NotificationLoggerService;
import io.github.msj.doccave_notify.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessor {

    private final NotificationService notificationService;
    private final NotificationLoggerService notificationLoggerService;

    @RabbitListener(queues = "reading-goal-reminder")
    public void processGoalReminder(NotificationDTO message) {
        log.info("📬 Processando notificação de meta para usuário {}", message.getUserId());

        try {
            notificationService.sendNotification(message);
            notificationLoggerService.logSuccess(message.getUserId(), message.getMessage());
        } catch (Exception e) {
            notificationLoggerService.logFailure(message.getUserId(), message.getMessage(), e.getMessage());
        }
    }

    @RabbitListener(queues = "weekly-summary")
    public void processWeeklySummary(NotificationDTO message) {
        log.info("📊 Processando relatório semanal para {}", message.getUserId());
        notificationService.sendNotification(message);
    }

}

