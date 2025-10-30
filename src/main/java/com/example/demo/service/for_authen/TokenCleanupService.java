package com.example.demo.service.for_authen;

import com.example.demo.entity.for_account.RefreshToken;
import com.example.demo.repository.for_authentication.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenCleanupService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void revokeExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<RefreshToken> expiredTokens = refreshTokenRepository
                .findAllByExpiredAtBeforeAndRevokedFalse(now);

        if (expiredTokens.isEmpty()) return;

        for (RefreshToken token : expiredTokens) {
            token.setRevoked(true);
        }
        refreshTokenRepository.saveAll(expiredTokens);
    }
}
