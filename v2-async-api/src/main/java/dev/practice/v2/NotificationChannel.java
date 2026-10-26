package dev.practice.v2;
public interface NotificationChannel {
    ChannelType type();
    void send(String recipient, String message) throws Exception;
}