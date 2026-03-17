package com.hector.orders.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    @NotBlank(message = "El nombre no puede quedar vacio")
    private String name;

    @NotBlank(message = "El origen no puede quedar vacio")
    private String origin;

    @Min(1)
    @NotNull(message = "El precio no puede ser nulo")
    private Double price;
    
}
