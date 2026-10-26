package dev.practice.v1;
import java.util.*;
import java.util.concurrent.*;

public class NotificationService {
    private final Map<ChannelType, NotificationChannel> channels;
    private final UserPreferenceRepository preferences;
    private final RecipientResolver recipients;
    private final ExecutorService executor;

    public NotificationService(Collection<NotificationChannel> adapters,
            UserPreferenceRepository preferences, RecipientResolver recipients, ExecutorService executor) {
        this.channels = new EnumMap<>(ChannelType.class);
        for (NotificationChannel adapter : adapters) {
            if (channels.putIfAbsent(adapter.type(), adapter) != null)
                throw new IllegalArgumentException("Duplicate channel: " + adapter.type());
        }
        this.preferences = preferences;
        this.recipients = recipients;
        this.executor = executor;
    }

    public List<DeliveryResult> sendNotification(String userId, String message) {
        List<Future<DeliveryResult>> futures = new ArrayList<>();
        // Submit EVERY task before waiting: Email cannot delay SMS execution.
        for (ChannelType type : new LinkedHashSet<>(preferences.getPreferredChannels(userId))) {
            futures.add(executor.submit(() -> {
                try {
                    NotificationChannel channel = channels.get(type);
                    if (channel == null) throw new IllegalArgumentException("Unsupported: " + type);
                    channel.send(recipients.recipientFor(userId, type), message);
                    return new DeliveryResult(type, true, null);
                } catch (Exception e) {
                    // Provider failure belongs to this channel, not the entire notification.
                    return new DeliveryResult(type, false, e.getMessage());
                }
            }));
        }
        List<DeliveryResult> results = new ArrayList<>();
        for (Future<DeliveryResult> future : futures) {
            try {
                results.add(future.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted", e);
            } catch (ExecutionException e) {
                throw new IllegalStateException("Unexpected task failure", e);
            }
        }
        return results;
    }
}