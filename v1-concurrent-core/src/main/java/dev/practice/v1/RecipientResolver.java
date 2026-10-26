package dev.practice.v1;
public interface RecipientResolver {
    String recipientFor(String userId, ChannelType channel);
}