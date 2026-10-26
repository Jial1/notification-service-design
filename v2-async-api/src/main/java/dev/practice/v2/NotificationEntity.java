package dev.practice.v2;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name = "notifications")
public class NotificationEntity {
    @Id public String id;
    @Column(nullable = false) public String userId;
    @Column(nullable = false, length = 4000) public String message;
    @Column(nullable = false) public Instant createdAt;
    protected NotificationEntity() {}
    public NotificationEntity(String id, String userId, String message) {
        this.id = id; this.userId = userId; this.message = message; this.createdAt = Instant.now();
    }
}