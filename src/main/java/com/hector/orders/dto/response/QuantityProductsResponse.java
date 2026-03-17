package com.hector.orders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuantityProductsResponse {
    private Long productId;
    private Long quantity;

}
