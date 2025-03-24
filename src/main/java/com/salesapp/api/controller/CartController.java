package com.salesapp.api.controller;

import com.salesapp.api.dto.request.CartItemRequest;
import com.salesapp.api.dto.request.UpdateCartItemRequest;
import com.salesapp.api.dto.response.CartResponse;
import com.salesapp.api.dto.response.MessageResponse;
import com.salesapp.api.security.services.UserDetailsImpl;
import com.salesapp.api.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Get current user's cart
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartResponse cart = cartService.getCart(userDetails.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**
     * Add an item to the cart
     */
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CartItemRequest request) {
        CartResponse cart = cartService.addToCart(userDetails.getId(), request);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**
     * Update cart item quantity
     */
    @PutMapping("/items")
    public ResponseEntity<CartResponse> updateCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UpdateCartItemRequest request) {
        CartResponse cart = cartService.updateCartItem(userDetails.getId(), request);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**
     * Remove item from cart
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cartItemId) {
        CartResponse cart = cartService.removeCartItem(userDetails.getId(), cartItemId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**
     * Clear the cart
     */
    @DeleteMapping
    public ResponseEntity<CartResponse> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartResponse cart = cartService.clearCart(userDetails.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}