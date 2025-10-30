package com.example.demo.config;

import com.example.demo.service.for_authen.TokenCleanupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenScheduler {

    private final TokenCleanupService cleanupService;

    public RefreshTokenScheduler(TokenCleanupService cleanupService) {
        this.cleanupService = cleanupService;
    }

    // Chạy mỗi 1 phút (60000 ms)
    @Scheduled(fixedRate = 60000)
    public void cleanExpiredTokens() {
        cleanupService.revokeExpiredTokens();
    }
}
