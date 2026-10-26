package dev.practice.v1;
import java.util.*;
import java.util.concurrent.*;

public class DemoApplication {
    public static void main(String[] args) {
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            NotificationChannel email = new NotificationChannel() {
                public ChannelType type() { return ChannelType.EMAIL; }
                public void send(String recipient, String message) throws Exception {
                    Thread.sleep(2000);
                    System.out.println("EMAIL -> " + recipient + ": " + message);
                }
            };
            NotificationChannel sms = new NotificationChannel() {
                public ChannelType type() { return ChannelType.SMS; }
                public void send(String recipient, String message) throws Exception {
                    Thread.sleep(100);
                    System.out.println("SMS -> " + recipient + ": " + message);
                }
            };
            NotificationService service = new NotificationService(
                List.of(email, sms),
                user -> List.of(ChannelType.EMAIL, ChannelType.SMS),
                (user, type) -> type == ChannelType.EMAIL ? user + "@example.com" : "+15550000000",
                executor);
            long start = System.currentTimeMillis();
            System.out.println(service.sendNotification("alice", "Hello!"));
            System.out.println("Elapsed ms: " + (System.currentTimeMillis() - start));
        }
    }
}