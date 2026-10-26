package dev.practice.v2;
import java.util.List;
public record NotificationView(String notificationId, String userId, List<DeliveryView> deliveries) {}