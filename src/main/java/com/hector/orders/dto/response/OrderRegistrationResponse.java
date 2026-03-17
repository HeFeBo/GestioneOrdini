package com.hector.orders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRegistrationResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;

}
