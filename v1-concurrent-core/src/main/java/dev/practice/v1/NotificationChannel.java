package dev.practice.v1;
public interface NotificationChannel {
    ChannelType type();
    void send(String recipient, String message) throws Exception;
}