package com.hector.orders.dto.response;

import java.time.LocalDateTime;

import com.hector.orders.model.ProductCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private long id;
    private String name;
    private ProductCategory category;
    private String origin;
    private double price;
    private double cost;
    private LocalDateTime purchaseDate;
    private LocalDateTime saleDate;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductResponse other = (ProductResponse) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
