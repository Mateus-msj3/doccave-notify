package io.github.msj.doccave_notify.repository;

import io.github.msj.doccave_notify.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {

    @Query("SELECT nl FROM NotificationLog nl WHERE nl.status = 'FAILED'")
    List<NotificationLog> findFailedNotifications();

}
