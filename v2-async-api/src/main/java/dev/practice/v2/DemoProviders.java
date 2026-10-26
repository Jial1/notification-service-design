package dev.practice.v2;
import org.springframework.stereotype.Component;
class DemoProviders {
    @Component static class Email implements NotificationChannel {
        public ChannelType type() { return ChannelType.EMAIL; }
        public void send(String recipient, String message) throws Exception {
            Thread.sleep(2000);
            System.out.println("EMAIL -> " + recipient + ": " + message);
        }
    }
    @Component static class Sms implements NotificationChannel {
        public ChannelType type() { return ChannelType.SMS; }
        public void send(String recipient, String message) throws Exception {
            Thread.sleep(100);
            System.out.println("SMS -> " + recipient + ": " + message);
        }
    }
    @Component static class Push implements NotificationChannel {
        public ChannelType type() { return ChannelType.PUSH; }
        public void send(String recipient, String message) {
            System.out.println("PUSH -> " + recipient + ": " + message);
        }
    }
}