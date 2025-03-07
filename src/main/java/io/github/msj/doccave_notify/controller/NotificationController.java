package io.github.msj.doccave_notify.controller;

import io.github.msj.doccave_notify.dto.NotificationDTO;
import io.github.msj.doccave_notify.dto.ReadingGoalResponseDTO;
import io.github.msj.doccave_notify.publisher.NotificationPublisher;
import io.github.msj.doccave_notify.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    private final NotificationPublisher notificationPublisher;

    @RequestMapping("/notify")
    public ResponseEntity<List<ReadingGoalResponseDTO>> notifyDocCave() {
        return ResponseEntity.ok(notificationService.findReadingGoals());
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendTestNotification() {
        NotificationDTO message = NotificationDTO.builder()
                .userId(UUID.randomUUID())
                .message("Lembre-se de estudar hoje!")
                .timestamp(LocalDateTime.now())
                .status("PENDING")
                .errorDetails(null)
                .build();

        notificationPublisher.sendNotification(message);
        return ResponseEntity.ok("Notificação enviada para a fila!");
    }

}
