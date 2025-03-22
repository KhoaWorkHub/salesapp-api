package com.salesapp.api.security.services;

import com.salesapp.api.entity.BlacklistedToken;
import com.salesapp.api.repository.BlacklistedTokenRepository;
import com.salesapp.api.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TokenBlacklistService {
    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Blacklist a token until its expiration
     */
    @Transactional
    public void blacklistToken(String token) {
        // Extract token expiration
        Date expiryDate = jwtUtils.getExpirationDateFromJwtToken(token);

        // Add to blacklist
        BlacklistedToken blacklistedToken = new BlacklistedToken(token, expiryDate);
        blacklistedTokenRepository.save(blacklistedToken);
    }

    /**
     * Check if a token is blacklisted
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

    /**
     * Clean up expired blacklisted tokens (runs daily)
     */
    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    @Transactional
    public void purgeExpiredTokens() {
        Date now = new Date();
        blacklistedTokenRepository.findAllExpired(now)
                .forEach(blacklistedTokenRepository::delete);
    }
}