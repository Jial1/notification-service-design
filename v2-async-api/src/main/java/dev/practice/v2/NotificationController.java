package dev.practice.v2;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;
    public NotificationController(NotificationService service) { this.service = service; }
    public record CreateRequest(@NotBlank String userId, @NotBlank String message) {}
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody CreateRequest request) {
        String id = service.submit(request.userId(), request.message());
        return ResponseEntity.accepted()
            .location(URI.create("/notifications/" + id))
            .body(Map.of("notificationId", id, "status", "PENDING"));
    }
    @GetMapping("/{id}")
    public NotificationView status(@PathVariable String id) { return service.status(id); }
    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(NotificationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }
}