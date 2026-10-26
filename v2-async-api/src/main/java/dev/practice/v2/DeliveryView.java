package dev.practice.v2;
public record DeliveryView(String deliveryId, ChannelType channel, DeliveryStatus status, int attempts, String error) {}