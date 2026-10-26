package dev.practice.v2;
import jakarta.persistence.*;
@Entity @Table(name = "deliveries", uniqueConstraints = @UniqueConstraint(columnNames = {"notification_id", "channel"}))
public class DeliveryEntity {
    @Id public String id;
    @Column(name = "notification_id", nullable = false) public String notificationId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) public ChannelType channel;
    @Enumerated(EnumType.STRING) @Column(nullable = false) public DeliveryStatus status;
    @Column(nullable = false) public int attempts;
    @Column(length = 500) public String error;
    protected DeliveryEntity() {}
    public DeliveryEntity(String id, String notificationId, ChannelType channel) {
        this.id = id; this.notificationId = notificationId; this.channel = channel;
        this.status = DeliveryStatus.PENDING; this.attempts = 0;
    }
}