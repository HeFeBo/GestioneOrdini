package com.hector.orders.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private long id;
    private String name;
    private String surname;
    private String idCard;
    private String cellPhone;
    private String address;
    private List<Long> idOrders;

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
        CustomerResponse other = (CustomerResponse) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
}
