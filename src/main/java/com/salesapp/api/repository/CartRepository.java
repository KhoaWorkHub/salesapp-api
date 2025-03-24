package com.salesapp.api.repository;

import com.salesapp.api.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserUserIdAndStatus(Long userId, String status);
    boolean existsByUserUserIdAndStatus(Long userId, String status);
}