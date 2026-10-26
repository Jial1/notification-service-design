package dev.practice.v2;
public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String id) { super("Notification not found: " + id); }
}