package com.hector.orders.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    @NotNull(message = "El id del cliente no puede ser nulo")
    private Long customerId;

}
