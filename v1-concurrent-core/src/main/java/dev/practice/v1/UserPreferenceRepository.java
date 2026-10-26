package dev.practice.v1;
import java.util.List;
public interface UserPreferenceRepository {
    List<ChannelType> getPreferredChannels(String userId);
}