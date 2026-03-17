package com.hector.orders.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRegistrationRequest {
    @NotNull(message = "El id del pedido no puede ser nulo")
    private Long orderId;

    @NotNull(message = "El id del producto no puede ser nulo")
    private Long productId;

    @Min(1)
    @NotNull(message = "La cantidad no puede ser nula")
    private Integer quantity;

}
