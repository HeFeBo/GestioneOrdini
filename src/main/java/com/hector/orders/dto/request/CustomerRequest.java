package com.hector.orders.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {
    @NotBlank(message = "El nombre no puede quedar vacio")
    private String name;

    @NotBlank(message = "El dni no puede quedar vacio")
    private String idCard;

}
