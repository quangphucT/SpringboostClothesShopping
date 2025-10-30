package com.example.demo.service.for_authen;

import com.example.demo.entity.for_account.Account;
import com.example.demo.entity.for_account.RefreshToken;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.TokenExpiredException;
import com.example.demo.exception.TokenInValidException;
import com.example.demo.exception.TokenRevokedException;
import com.example.demo.repository.for_authentication.AuthenticationRepository;
import com.example.demo.repository.for_authentication.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;


    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Autowired
    private HttpServletRequest request;

    private String getClientIp() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private String getDeviceInfo() {
        return request.getHeader("User-Agent");
    }
    public String generateAccessToken(Account account) {
        String accessToken = Jwts.builder()
                .subject(account.getId() +"")
                .claim("role", account.getRole().name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60 * 260)) // 1 tiếng
                .signWith(getSignInKey())
                .compact();
        return accessToken;
    }

    public String generateRefreshToken(Account account) {
        String refreshToken = Jwts.builder()
                .subject(account.getId() + "")
                .claim("role", account.getRole().name())
                .claim("deviceInfo", getDeviceInfo())
                .claim("ipAddress", getClientIp())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2)) // 2 phút
                .signWith(getSignInKey())
                .compact();

        RefreshToken entity = new RefreshToken();
        entity.setToken(refreshToken);
        entity.setAccount(account);
        entity.setCreatedAt(new Date());
        entity.setExpiredAt(new Date(System.currentTimeMillis() + 1000 * 60 * 2));
        entity.setRevoked(false);
        entity.setDeviceInfo(getDeviceInfo());
        entity.setIpAddress( getClientIp() );
        refreshTokenRepository.save(entity);

        return refreshToken;
    }


    public Account verifyAccountThroughToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String idString = claims.getSubject();
        long id = Long.parseLong(idString);
        return authenticationRepository.findAccountById(id);
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        Claims claims;
       try {
              claims = Jwts.parser()
                   .verifyWith(getSignInKey())
                   .build()
                   .parseSignedClaims(refreshToken)
                   .getPayload();

       } catch (ExpiredJwtException e) {
           throw new TokenExpiredException("Refresh token expired");
       } catch (JwtException e) {
           throw new TokenInValidException("Invalid refresh token");
       }

        long accountId   = Long.parseLong(claims.getSubject());

        String deviceInformation = claims.get("deviceInfo",  String.class);

        RefreshToken refreshTokenInDB = refreshTokenRepository.findByAccountIdAndDeviceInfoAndToken(accountId, deviceInformation, refreshToken).orElseThrow(() -> new NotFoundException("RefreshToken not found!"));


        if(refreshTokenInDB.isRevoked()){
            throw new TokenRevokedException("Refresh token revoked!");
        }

        return refreshTokenInDB;
    }

}
