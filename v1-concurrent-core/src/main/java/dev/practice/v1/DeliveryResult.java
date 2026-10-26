package dev.practice.v1;
public record DeliveryResult(ChannelType channel, boolean success, String error) {}