package com.hector.orders.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "Il id del cliente non puó essere nullo")
    private long customerId;

    @NotNull(message = "La data di emissione non puó essere nulla")
    @FutureOrPresent(message = "La data di emissione non puó essere passata")
    private LocalDateTime issueDate;

}
