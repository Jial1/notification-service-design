package dev.practice.v2;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {}