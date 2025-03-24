package com.salesapp.api.repository;

import com.salesapp.api.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartCartIdAndProductProductId(Long cartId, Long productId);
    void deleteByCartCartId(Long cartId);
}