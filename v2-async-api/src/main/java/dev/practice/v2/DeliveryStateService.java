package dev.practice.v2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class DeliveryStateService {
    private final DeliveryRepository deliveries;
    public DeliveryStateService(DeliveryRepository deliveries) { this.deliveries = deliveries; }
    @Transactional
    public boolean claim(String id) {
        return deliveries.claim(id, DeliveryStatus.PENDING, DeliveryStatus.PROCESSING) == 1;
    }
    @Transactional
    public void finish(String id, DeliveryStatus status, String error) {
        deliveries.finish(id, status, error);
    }
}