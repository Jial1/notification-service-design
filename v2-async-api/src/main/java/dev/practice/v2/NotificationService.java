package dev.practice.v2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.*;
import java.util.concurrent.*;
@Service
public class NotificationService {
    private final NotificationRepository notifications;
    private final DeliveryRepository deliveries;
    private final UserPreferences preferences;
    private final DeliveryProcessor processor;
    private final ExecutorService executor;
    public NotificationService(NotificationRepository notifications, DeliveryRepository deliveries,
            UserPreferences preferences, DeliveryProcessor processor, ExecutorService notificationExecutor) {
        this.notifications = notifications; this.deliveries = deliveries;
        this.preferences = preferences; this.processor = processor; this.executor = notificationExecutor;
    }
    @Transactional
    public String submit(String userId, String message) {
        String notificationId = UUID.randomUUID().toString();
        notifications.save(new NotificationEntity(notificationId, userId, message));
        List<String> deliveryIds = preferences.channelsFor(userId).stream().distinct().map(type -> {
            String id = UUID.randomUUID().toString();
            deliveries.save(new DeliveryEntity(id, notificationId, type));
            return id;
        }).toList();
        // Workers must not see uncommitted rows. WARNING: this is NOT durable scheduling.
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                for (String id : deliveryIds) {
                    try { executor.submit(() -> processor.process(id)); }
                    catch (RejectedExecutionException e) {
                        // Pending record remains, but there is no automatic recovery in V2.
                        System.err.println("Executor rejected delivery " + id);
                    }
                }
            }
        });
        return notificationId;
    }
    @Transactional(readOnly = true)
    public NotificationView status(String id) {
        NotificationEntity notification = notifications.findById(id)
            .orElseThrow(() -> new NotificationNotFoundException(id));
        List<DeliveryView> items = deliveries.findByNotificationIdOrderByChannelAsc(id).stream()
            .map(d -> new DeliveryView(d.id, d.channel, d.status, d.attempts, d.error)).toList();
        return new NotificationView(notification.id, notification.userId, items);
    }
}