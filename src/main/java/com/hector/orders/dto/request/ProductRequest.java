package com.hector.orders.dto.request;

import java.time.LocalDateTime;

import com.hector.orders.model.ProductCategory;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "il nome non può rimanere vuoto")
    private String name;

    @NotNull(message = "La categoria non può essere nulla")
    private ProductCategory category;

    private String origin;

    @DecimalMin(value = "0.01")
    private double price;

    @DecimalMin(value = "0.01")
    private double cost;

    @NotNull(message = "La data di aquisto non puó essere nulla")
    @PastOrPresent(message = "La data di aquisto non puó essere futura")
    private LocalDateTime purchaseDate;
    
}
