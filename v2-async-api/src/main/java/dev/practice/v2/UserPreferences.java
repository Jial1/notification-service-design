package dev.practice.v2;
import java.util.List;
import org.springframework.stereotype.Component;
@Component
public class UserPreferences {
    public List<ChannelType> channelsFor(String userId) {
        if ("alice".equals(userId)) return List.of(ChannelType.EMAIL, ChannelType.SMS);
        if ("bob".equals(userId)) return List.of(ChannelType.SMS);
        return List.of(ChannelType.EMAIL);
    }
    public String recipientFor(String userId, ChannelType type) {
        return switch (type) {
            case EMAIL -> userId + "@example.com";
            case SMS -> "+15550000000";
            case PUSH -> "device-" + userId;
        };
    }
}