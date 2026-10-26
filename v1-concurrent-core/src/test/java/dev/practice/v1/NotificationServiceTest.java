package dev.practice.v1;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {
    @Test void failureInEmailDoesNotStopSms() {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            NotificationChannel email = new NotificationChannel() {
                public ChannelType type() { return ChannelType.EMAIL; }
                public void send(String to, String msg) { throw new IllegalStateException("down"); }
            };
            NotificationChannel sms = new NotificationChannel() {
                public ChannelType type() { return ChannelType.SMS; }
                public void send(String to, String msg) {}
            };
            NotificationService service = new NotificationService(List.of(email, sms),
                user -> List.of(ChannelType.EMAIL, ChannelType.SMS),
                (user, type) -> user, executor);
            List<DeliveryResult> results = service.sendNotification("alice", "hello");
            assertEquals(2, results.size());
            assertFalse(results.get(0).success());
            assertTrue(results.get(1).success());
        }
    }
}