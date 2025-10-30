package com.example.demo.repository.for_authentication;
import com.example.demo.entity.for_account.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteAllByExpiredAtBefore(Date now);
    Optional<RefreshToken> findByAccountIdAndDeviceInfoAndToken(long accountId, String deviceInfo, String token);
    List<RefreshToken> findAllByExpiredAtBeforeAndRevokedFalse(LocalDateTime now);
}
