package com.salesapp.api.repository;

import com.salesapp.api.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByToken(String token);

    @Query("SELECT b FROM BlacklistedToken b WHERE b.expiryDate < :now")
    List<BlacklistedToken> findAllExpired(Date now);
}