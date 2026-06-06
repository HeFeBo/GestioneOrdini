package com.hector.orders.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @NotNull(message = "I'id del ordine non puó essere nullo")
    private long orderId;

    @NotNull(message = "I'id del prodotto non puó essere nullo")
    private long productId;

    @Min(1)
    private int quantity;

}
