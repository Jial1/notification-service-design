package dev.practice.v2;
import org.springframework.stereotype.Component;
@Component
public class DeliveryProcessor {
    private final DeliveryRepository deliveries;
    private final NotificationRepository notifications;
    private final DeliveryStateService state;
    private final ChannelRegistry channels;
    private final UserPreferences preferences;
    public DeliveryProcessor(DeliveryRepository deliveries, NotificationRepository notifications,
                             DeliveryStateService state, ChannelRegistry channels, UserPreferences preferences) {
        this.deliveries = deliveries; this.notifications = notifications;
        this.state = state; this.channels = channels; this.preferences = preferences;
    }
    public void process(String deliveryId) {
        if (!state.claim(deliveryId)) return; // Atomic conditional update prevents concurrent claims.
        try {
            DeliveryEntity delivery = deliveries.findById(deliveryId).orElseThrow();
            NotificationEntity notification = notifications.findById(delivery.notificationId).orElseThrow();
            channels.get(delivery.channel).send(
                preferences.recipientFor(notification.userId, delivery.channel), notification.message);
            state.finish(deliveryId, DeliveryStatus.SUCCESS, null);
        } catch (Exception e) {
            state.finish(deliveryId, DeliveryStatus.FAILED, "Delivery attempt failed");
        }
    }
}