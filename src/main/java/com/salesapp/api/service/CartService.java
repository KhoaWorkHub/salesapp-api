package com.salesapp.api.service;

import com.salesapp.api.dto.request.CartItemRequest;
import com.salesapp.api.dto.request.UpdateCartItemRequest;
import com.salesapp.api.dto.response.CartItemResponse;
import com.salesapp.api.dto.response.CartResponse;
import com.salesapp.api.entity.Cart;
import com.salesapp.api.entity.CartItem;
import com.salesapp.api.entity.Product;
import com.salesapp.api.entity.User;
import com.salesapp.api.exception.ResourceNotFoundException;
import com.salesapp.api.repository.CartItemRepository;
import com.salesapp.api.repository.CartRepository;
import com.salesapp.api.repository.ProductRepository;
import com.salesapp.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get the active cart for a user, creating one if it doesn't exist
     */
    @Transactional
    public Cart getOrCreateActiveCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return cartRepository.findByUserUserIdAndStatus(userId, "ACTIVE")
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setStatus("ACTIVE");
                    return cartRepository.save(newCart);
                });
    }

    /**
     * Add a product to the cart
     */
    @Transactional
    public CartResponse addToCart(Long userId, CartItemRequest request) {
        Cart cart = getOrCreateActiveCart(userId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        // Check if product already exists in cart
        CartItem cartItem = cartItemRepository.findByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId())
                .orElse(null);

        if (cartItem == null) {
            // Create new cart item
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setPrice(product.getPrice());
            cartItem.setQuantity(request.getQuantity());
            cart.addCartItem(cartItem);
        } else {
            // Update existing cart item
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cart.recalculateTotalPrice();
        }

        cartRepository.save(cart);
        return convertToCartResponse(cart);
    }

    /**
     * Update cart item quantity
     */
    @Transactional
    public CartResponse updateCartItem(Long userId, UpdateCartItemRequest request) {
        Cart cart = getOrCreateActiveCart(userId);

        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + request.getCartItemId()));

        // Verify cart item belongs to user's cart
        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            throw new IllegalArgumentException("Cart item does not belong to user's cart");
        }

        cartItem.setQuantity(request.getQuantity());
        cart.recalculateTotalPrice();
        cartRepository.save(cart);

        return convertToCartResponse(cart);
    }

    /**
     * Remove an item from the cart
     */
    @Transactional
    public CartResponse removeCartItem(Long userId, Long cartItemId) {
        Cart cart = getOrCreateActiveCart(userId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        // Verify cart item belongs to user's cart
        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            throw new IllegalArgumentException("Cart item does not belong to user's cart");
        }

        cart.removeCartItem(cartItem);
        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);

        return convertToCartResponse(cart);
    }

    /**
     * Clear the cart
     */
    @Transactional
    public CartResponse clearCart(Long userId) {
        Cart cart = getOrCreateActiveCart(userId);

        // Remove all items from the cart
        List<CartItem> itemsToRemove = cart.getCartItems();
        cartItemRepository.deleteAll(itemsToRemove);

        cart.clearCart();
        cartRepository.save(cart);

        return convertToCartResponse(cart);
    }

    /**
     * Get the current cart
     */
    public CartResponse getCart(Long userId) {
        Cart cart = getOrCreateActiveCart(userId);
        return convertToCartResponse(cart);
    }

    /**
     * Convert Cart entity to CartResponse DTO
     */
    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setCartId(cart.getCartId());
        response.setUserId(cart.getUser().getUserId());
        response.setStatus(cart.getStatus());
        response.setTotalPrice(cart.getTotalPrice());

        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(this::convertToCartItemResponse)
                .collect(Collectors.toList());

        response.setItems(items);
        response.setItemCount(items.size());

        return response;
    }

    /**
     * Convert CartItem entity to CartItemResponse DTO
     */
    private CartItemResponse convertToCartItemResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        response.setCartItemId(item.getCartItemId());
        response.setProductId(item.getProduct().getProductId());
        response.setProductName(item.getProduct().getProductName());
        response.setProductImage(item.getProduct().getImageURL());
        response.setPrice(item.getPrice());
        response.setQuantity(item.getQuantity());
        response.setSubtotal(item.getSubtotal());
        return response;
    }
}