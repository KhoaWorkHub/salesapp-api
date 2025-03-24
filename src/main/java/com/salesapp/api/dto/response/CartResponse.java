package com.salesapp.api.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private Long cartId;
    private Long userId;
    private String status;
    private BigDecimal totalPrice;
    private List<CartItemResponse> items;
    private int itemCount;
}