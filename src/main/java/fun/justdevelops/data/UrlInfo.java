package fun.justdevelops.data;

import java.time.LocalDateTime;
import java.util.UUID;

public class UrlInfo {
    private UUID userId;
    private String fullUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private int usageLimit;

    public int getUsageCount() {
        return usageCount;
    }

    private int usageCount;


    public UrlInfo(UUID userId, String fullUrl, int usageLimit, LocalDateTime expiresAt) {
        this.userId = userId;
        this.fullUrl = fullUrl;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
        this.usageLimit = usageLimit;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void use() {
        this.usageCount++;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isValid() {
        return usageCount < usageLimit && expiresAt.isAfter(createdAt);
    }

    public UUID getUserId() {
        return userId;
    }
}
