package com.hector.orders.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    @NotBlank(message = "Il nome non puó rimanere vuoto")
    private String name;

    @NotBlank(message = "Il cognome non puó rimanere vuoto")
    private String surname;

    @NotBlank(message = "Il numero di la carta d'identitá non puó rimanere vuoto")
    private String idCard;

    @NotBlank(message = "Il numero di cellulare non puó rimanere vuoto")
    private String cellPhone;

    @NotBlank(message = "I' indirizzo non puó rimanere vuoto")
    private String address;

}
