package dev.practice.v2;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class ChannelRegistryTest {
    @Test void resolvesRegisteredChannel() {
        NotificationChannel email = new NotificationChannel() {
            public ChannelType type() { return ChannelType.EMAIL; }
            public void send(String recipient, String message) {}
        };
        assertSame(email, new ChannelRegistry(List.of(email)).get(ChannelType.EMAIL));
    }
}