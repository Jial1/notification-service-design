package dev.practice.v2;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, String> {
    List<DeliveryEntity> findByNotificationIdOrderByChannelAsc(String notificationId);
    @Modifying
    @Query("update DeliveryEntity d set d.status = :next, d.attempts = d.attempts + 1 where d.id = :id and d.status = :expected")
    int claim(@Param("id") String id, @Param("expected") DeliveryStatus expected, @Param("next") DeliveryStatus next);
    @Modifying
    @Query("update DeliveryEntity d set d.status = :status, d.error = :error where d.id = :id")
    int finish(@Param("id") String id, @Param("status") DeliveryStatus status, @Param("error") String error);
}