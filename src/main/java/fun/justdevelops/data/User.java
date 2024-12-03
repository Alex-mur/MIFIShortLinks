package fun.justdevelops.data;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private UUID id;
    private LocalDateTime createdAt;

    public User() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
