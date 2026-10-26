package dev.practice.v2;
import java.util.*;
import org.springframework.stereotype.Component;
@Component
public class ChannelRegistry {
    private final Map<ChannelType, NotificationChannel> channels = new EnumMap<>(ChannelType.class);
    public ChannelRegistry(List<NotificationChannel> adapters) {
        for (NotificationChannel adapter : adapters)
            if (channels.putIfAbsent(adapter.type(), adapter) != null)
                throw new IllegalArgumentException("Duplicate channel: " + adapter.type());
    }
    public NotificationChannel get(ChannelType type) {
        NotificationChannel channel = channels.get(type);
        if (channel == null) throw new IllegalArgumentException("Unsupported channel: " + type);
        return channel;
    }
}