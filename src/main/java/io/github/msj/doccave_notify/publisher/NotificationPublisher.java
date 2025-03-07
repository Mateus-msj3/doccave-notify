package io.github.msj.doccave_notify.publisher;

import io.github.msj.doccave_notify.dto.NotificationDTO;
import io.github.msj.doccave_notify.dto.ReadingGoalResponseDTO;
import io.github.msj.doccave_notify.record.NotificationMessage;
import io.github.msj.doccave_notify.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisher {

    private final NotificationService notificationService;

    private final RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 0 9 * * ?") // Roda todos os dias às 9h
    public void checkAndPublishNotifications() {
        List<ReadingGoalResponseDTO> activeGoals = notificationService.findReadingGoals();

        for (ReadingGoalResponseDTO goal : activeGoals) {
            int progress = (goal.getGoalPages() * 100) / goal.getPagesRead();

            if (progress < 50 && goal.getEndDate().isBefore(LocalDate.now().plusDays(5))) {
                NotificationMessage message = new NotificationMessage(goal.getUser().getId(), "📚 Sua meta está atrasada!");
                log.info("📩 Publicando notificação para fila: {}", message);

                rabbitTemplate.convertAndSend("reading-goal-reminder", message);
            }
        }
    }

    public void sendNotification(NotificationDTO message) {
        log.info("📩 Enviando notificação para a fila: {}", message);
        rabbitTemplate.convertAndSend("reading-goal-reminder", message);
    }
}

